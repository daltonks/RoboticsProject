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
        private const string OUTPUT_VIDEO_PATH = "output/Walk1.mpg";

        static void Main(string[] args)
        {
            Directory.CreateDirectory(OUTPUT_DIRECTORY);

            using (var reader = new VideoFrameReader(INPUT_VIDEO_PATH))
            {
                using (var writer = new VideoFileWriter())
                {
                    writer.Open(
                        OUTPUT_VIDEO_PATH,
                        reader.UnderlyingReader.Width,
                        reader.UnderlyingReader.Height
                    );

                    Bitmap lastBitmap = null;
                    reader.ReadAllFrames(
                        (bitmap, index) =>
                        {
                            if (lastBitmap != null)
                            {
                                bitmap.SetPixel(0, 0, Color.Red);
                                lastBitmap.Dispose();
                            }
                            writer.WriteVideoFrame(bitmap);
                            lastBitmap = bitmap;
                        }
                    );
                    lastBitmap.Dispose();
                }
            }
        }
    }
}
