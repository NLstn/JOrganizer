# JOrganizer

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

I would be really greatful about any feedback about issues you found or ideas to improve the program.

## Configuration files

JMediaOrganizer stores its generated configuration files in the `.jmediaOrganizer` directory inside your user home (for example, `~/.jmediaOrganizer` on Linux and macOS or `C:\Users\&lt;username&gt;\.jmediaOrganizer` on Windows). The folder is created automatically when the application starts and populated with default configuration files if they are missing.
