package org.jaudiotagger.audio.mp4;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.mp4.atom.Mp4MetaBox;
import org.jaudiotagger.audio.mp4.atom.Mp4BoxHeader;
import org.jaudiotagger.audio.mp4.atom.AbstractMp4Box;
import org.jaudiotagger.audio.mp4.atom.Mp4StcoBox;
import org.jaudiotagger.logging.ErrorMessage;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tree representing atoms in the mp4 file
 *
 * Note it doesnt create the complete tree it delves into subtrees for atom we know about and are interested in. (Note
 * it would be impossible to create a complete tree fro any file without understanding all the nodes because
 * some atoms such as meta contain data and children and therefore need to be specially preprocessed)
 *
 * This class is currently only used when writing tags because it better handles the diffciulties of mdat aand free
 * atoms being optional/multiple places then the older sequentail method. It is expected this class will eventauuly
 * be used when reading tags as well.
 *
 * Uses a TreeModel for the tree, with convienience methods holding onto references to most common nodes so they
 * can be used without having to traverse the tree again.
 */
public class Mp4AtomTree
{
    private DefaultMutableTreeNode  rootNode;
    private DefaultTreeModel        dataTree;
    private DefaultMutableTreeNode  moovNode;
    private DefaultMutableTreeNode  mdatNode;
    private DefaultMutableTreeNode  stcoNode;
    private DefaultMutableTreeNode  ilstNode;
    private DefaultMutableTreeNode  metaNode;
    private List<DefaultMutableTreeNode> freeNodes = new ArrayList<DefaultMutableTreeNode > ();
    private Mp4StcoBox              stco;
    private ByteBuffer              moovBuffer; //Contains all the data under moov
    private Mp4BoxHeader            moovHeader;

    //Logger Object
    public static Logger logger = Logger.getLogger("org.jaudiotagger.audio.mp4");

    /**
     * Create Atom Tree
     *
     * @param raf
     * @throws IOException
     * @throws CannotReadException
     */
    public Mp4AtomTree(RandomAccessFile raf) throws IOException, CannotReadException
    {
        buildTree(raf,true);
    }

    /**
     * Create Atom Tree and maintain open channel to raf, should only be used if will continue
     * to use raf after this call, you will have to close raf yourself.
     *
     * @param raf
     * @param closeOnExit to keep randomfileaccess open, only used when randomaccessfile already being used
     * @throws IOException
     * @throws CannotReadException
     */
    public Mp4AtomTree(RandomAccessFile raf,boolean closeOnExit) throws IOException, CannotReadException
    {
        buildTree(raf,closeOnExit);
    }

    /** Build a tree of the atoms in the file
     *
     * @param raf
     * @param closeExit false to keep randomfileacces open, only used when randomaccessfile already being used
     * @return
     * @throws java.io.IOException
     */
    public DefaultTreeModel buildTree(RandomAccessFile raf,boolean closeExit)throws IOException, CannotReadException
    {
        FileChannel fc=null;
        try
        {
            fc = raf.getChannel();

            //make sure at start of file
            fc.position(0);
            
            //Build up map of nodes
            rootNode  = new DefaultMutableTreeNode();
            dataTree  = new DefaultTreeModel(rootNode);

            //Iterate though all the top level Nodes
            while(fc.position()<fc.size())
            {
                Mp4BoxHeader boxHeader = new Mp4BoxHeader();
                ByteBuffer headerBuffer = ByteBuffer.allocate(Mp4BoxHeader.HEADER_LENGTH);
                fc.read(headerBuffer);
                headerBuffer.rewind();
                boxHeader.update(headerBuffer);
                boxHeader.setFilePos(fc.position() - Mp4BoxHeader.HEADER_LENGTH);
                DefaultMutableTreeNode newAtom = new DefaultMutableTreeNode(boxHeader);

                //Go down moov
                if(boxHeader.getId().equals(Mp4NotMetaFieldKey.MOOV.getFieldName()))
                {
                    moovNode=newAtom;
                    moovHeader=boxHeader;

                    long filePosStart = fc.position();
                    moovBuffer = ByteBuffer.allocate(boxHeader.getDataLength());
                    fc.read(moovBuffer);
                    moovBuffer.rewind();
                    buildChildrenOfNode(moovBuffer,newAtom);
                    fc.position(filePosStart);
                }
                else if(boxHeader.getId().equals(Mp4NotMetaFieldKey.FREE.getFieldName()))
                {
                    //Might be multiple in different locations
                    freeNodes.add(newAtom);
                }
                else if(boxHeader.getId().equals(Mp4NotMetaFieldKey.MDAT.getFieldName()))
                {
                    if(mdatNode!=null)
                    {
                        throw new CannotReadException(ErrorMessage.MP4_FILE_CONTAINS_MULTIPLE_DATA_ATOMS.getMsg());    
                    }
                    mdatNode=newAtom;
                }
                rootNode.add(newAtom);
                fc.position(fc.position() + boxHeader.getDataLength());
            }
            return dataTree;
        }
        finally
        {
            if(closeExit)
            {
                fc.close();
            }
        }
    }

    /**
     * Display atom tree
     */
    public void printAtomTree()
    {
        Enumeration e = rootNode.preorderEnumeration();
        DefaultMutableTreeNode nextNode;
        while(e.hasMoreElements())
        {
            nextNode= (DefaultMutableTreeNode)e.nextElement();           
            Mp4BoxHeader header = (Mp4BoxHeader)nextNode.getUserObject();
            if(header!=null)
            {
                String tabbing = new String();
                for(int i=1;i<nextNode.getLevel();i++)
                {
                    tabbing+="\t";
                }
                System.out.println(tabbing+"Atom "+header.getId()+ " @ "+header.getFilePos()
                        +" of size:"+header.getLength() +" ,ends @ "+(header.getFilePos() + header.getLength()));
            }
        }
    }
    public void buildChildrenOfNode(ByteBuffer moovBuffer,DefaultMutableTreeNode parentNode)throws IOException, CannotReadException
    {
        //Preprocessing for nodes that contain data before their children atoms
        Mp4BoxHeader parentBoxHeader = (Mp4BoxHeader)parentNode.getUserObject();
        if(parentBoxHeader.getId().equals(Mp4NotMetaFieldKey.META.getFieldName()))
        {
            Mp4MetaBox meta = new Mp4MetaBox(parentBoxHeader, moovBuffer);
            meta.processData();
        }
        
        Mp4BoxHeader boxHeader;
        int startPos = moovBuffer.position();
        while(moovBuffer.position()<((startPos + parentBoxHeader.getDataLength()) -  Mp4BoxHeader.HEADER_LENGTH))
        {
            boxHeader = new Mp4BoxHeader(moovBuffer);
            if(boxHeader!=null)
            {   
                boxHeader.setFilePos(moovHeader.getFilePos() + moovBuffer.position());
                logger.finest("Atom "+boxHeader.getId()+ " @ "+boxHeader.getFilePos()
                        +" of size:"+boxHeader.getLength() +" ,ends @ "+(boxHeader.getFilePos() + boxHeader.getLength()));

                DefaultMutableTreeNode newAtom = new DefaultMutableTreeNode(boxHeader);
                if(boxHeader.getId().equals(Mp4NotMetaFieldKey.META.getFieldName()))
                {
                    metaNode=newAtom;
                }
                else if(boxHeader.getId().equals(Mp4NotMetaFieldKey.STCO.getFieldName()))
                {
                    if(stco==null)
                    {
                        stco = new Mp4StcoBox(boxHeader, moovBuffer);
                        stcoNode=newAtom;
                    }
                }
                else if(boxHeader.getId().equals(Mp4NotMetaFieldKey.ILST.getFieldName()))
                {
                    ilstNode=newAtom;

                }
                else if(boxHeader.getId().equals(Mp4NotMetaFieldKey.FREE.getFieldName()))
                {
                    //Might be multiple in different locations
                    freeNodes.add(newAtom);
                }

                if(
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.TRAK.getFieldName())) ||
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.MDIA.getFieldName())) ||
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.MINF.getFieldName())) ||
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.STBL.getFieldName())) ||
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.UDTA.getFieldName())) ||
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.META.getFieldName())) ||
                    (boxHeader.getId().equals(Mp4NotMetaFieldKey.ILST.getFieldName()))
                  )
                {
                    buildChildrenOfNode(moovBuffer,newAtom);
                }
                if(boxHeader.getId().equals(Mp4NotMetaFieldKey.META.getFieldName()))
                {
                    moovBuffer.position( moovBuffer.position() + (boxHeader.getDataLength() - Mp4MetaBox.FLAGS_LENGTH));                   
                }
                else
                {
                    moovBuffer.position( moovBuffer.position() + boxHeader.getDataLength());
                }
                parentNode.add(newAtom);
            }
        }
        moovBuffer.position(startPos);
    }
     

    public DefaultTreeModel getDataTree()
    {
        return dataTree;
    }


    public DefaultMutableTreeNode getMoovNode()
    {
        return moovNode;
    }

    public DefaultMutableTreeNode getStcoNode()
    {
        return stcoNode;
    }

    public DefaultMutableTreeNode getIlstNode()
    {
        return ilstNode;
    }

    public Mp4BoxHeader getBoxHeader(DefaultMutableTreeNode node)
    {
        if(node==null)
        {
            return null;
        }
        return (Mp4BoxHeader)node.getUserObject();
    }

    public DefaultMutableTreeNode getMdatNode()
    {
        return mdatNode;
    }

    public DefaultMutableTreeNode getMetaNode()
    {
        return metaNode;
    }

    public List<DefaultMutableTreeNode> getFreeNodes()
    {
        return freeNodes;
    }

    public Mp4StcoBox getStco()
    {
        return stco;
    }

    public ByteBuffer getMoovBuffer()
    {
        return moovBuffer;
    }

    public Mp4BoxHeader getMoovHeader()
    {
        return moovHeader;
    }
}
