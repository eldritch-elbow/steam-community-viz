steam-viz
=========

This repository contains a project for retrieving, storing, and visualizing data from 
the Steam Community API. It's self contained, so all you should need to do is clone it,
then import the <code>steam-viz</code> folder into an Eclipse workspace.

To run the scraper, execute the main method in the SteamScraper class. This requires two arguments: 

(1) an input file containing steam IDs to retreive data for, and 
(2) a prefix for an output file.

You'll also need to provide a system property <code>steam.viztools.apikey</code> containing a valid
Steam Community API key. 
   
*Notes:*

 * Currently, only Steam data retrieval is supported, via the SteamScraper class. 
 * All dependencies are currently packaged with the project. Will be moving to Maven in the future.
 * This project grew out of a quick throw-away, so there are currently no unit tests.
 * It's a work in progress :) Apologies for omissions, bugs etc. 

