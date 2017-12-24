# JMediaOrganizer

Hi guys,

This is a little program that i wrote actually for myself, but decided to make it (at least a bit) prettier and release it.

You can use it to define the new file path of your files in a pattern using the information stored inside the ID3Tags. As this sounds confusing even to me, i'll give you an example:

We have a song with the following tags:  
Title: Iridescent  
Track: 12  
Artist: Linkin Park  
Album: A thousand suns
Extension of the file is .mp3

Apart from that i also configured my output path (the path of my library) to be D:\Media\Music\. I personally use the following pattern:  
%output%%artist% - %album%\%title%%extension%

If i run the conversion now, the new path of this file would be:  
D:\Media\Music\Linkin Park - A Thousand Sunds\Iridescent.mp3

And yes, you guessed right, this is exactly the way Plex expects your music library to be. I use Plex, and i want to make my life maintaining my Plex library easier. This is what this program is about.

I would be really greatful about any feedback about issues you found or ideas to improve the program.
