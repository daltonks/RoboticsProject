using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Accord.Video.FFMPEG;

namespace ObjectTracker
{
    class Program
    {
        private const string INPUT_VIDEO_PATH = "input/Walk1.mpg";
        private const string OUTPUT_DIRECTORY = "output/";

        static void Main(string[] args)
        {
            Directory.CreateDirectory(OUTPUT_DIRECTORY);

            using (var frameReader = new VideoFrameReader(INPUT_VIDEO_PATH))
            {
                Console.WriteLine(frameReader.Width + ", " + frameReader.Height);
                var numFrames = frameReader.ReadAllFrames(
                    (bitmap, index) =>
                    {
                        using (bitmap)
                        {
                            bitmap.SetPixel(0, 0, Color.Red);
                            bitmap.Save($"{OUTPUT_DIRECTORY}{index}.bmp");
                        }
                    }
                );
            }
        }
    }
}
