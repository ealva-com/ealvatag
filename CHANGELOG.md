Change Log
==========

Version 0.0.2 *(2017-01-19)*
----------------------------

 * Create DumpHeap program that loads a single mp3, reads the audio header, and gets the field count to ensure the tag is read
 * Baseline snapshot - retained heap from AppClassLoader 409,992/25.25% opening single mp3
 * Many refactorings: 
   - In many classes, switch from Map<Integer, ?> to an array as the keys were primarily contiguous starting at zero. Much less space, 
   no Integer object creation, and lookup is much faster as it's a simple index
   - Start using Immutable collections, stop exposing implementation via public methods, remove implementation inheritance which caused 
   many unused and/or high-cost collections
   - Lazily construct numerous collections, many of which only were used in exceptional conditions.
   - Introduce factories for more lazy instantiation. Why pay the price for Vorbis reader/writer if I'm only reading/writing mp3?
   - Begin having preference for Null instances or Optional<>, over null. Make exceptions more consistent.
   - Lots of unnecessary code deleted. Much more can be done, especially test code
 * New snapshot - retained heap from AppClasssLoader 238,752/16.45. 42% memory reduction opening single mp3 file. Plus performance 
 improvements. All tests pass.
 * Todo - much more cleanup regarding null, exceptions, error messages. Need Okio for true caching of buffer memory - especially 
 important for batch operations. Need lots more tests. Moving them to Junit 4, cleaning test code.
 
Version 0.0.1 *(2017-01-08)*
----------------------------

 * Full fork from jaudiotagger https://bitbucket.org/ijabz/jaudiotagger
 * Re-host on GitHub
 * Update license to LGPLv3 from LGPLv2.1 so Apache License Version 2.0 libraries may be linked to this library 
