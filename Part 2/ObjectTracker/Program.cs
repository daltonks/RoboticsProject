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

            var numFrames = ReadAllFrames(
                filePath: INPUT_VIDEO_PATH,
                onFrameProcessed: (bitmap, index) =>
                {
                    bitmap.SetPixel(0, 0, Color.Red);
                    bitmap.Save($"{OUTPUT_DIRECTORY}{index}.bmp");
                }
            );
        }

        /// <summary>
        /// Processes all frames of a video
        /// </summary>
        /// <param name="filePath">Path to the video</param>
        /// <param name="onFrameProcessed">Provides a bitmap and an index of a frame that is processing</param>
        /// <returns>The total number of frames</returns>
        static int ReadAllFrames(string filePath, Action<Bitmap, int> onFrameProcessed)
        {
            using (var reader = new VideoFileReader())
            {
                reader.Open(filePath);
                Bitmap frameBitmap;
                int index = 0;
                while ((frameBitmap = reader.ReadVideoFrame()) != null)
                {
                    using (frameBitmap)
                    {
                        onFrameProcessed.Invoke(frameBitmap, index);
                        
                    }
                    index++;
                }
                
                var frameCount = index;
                return frameCount;
            }
        }
    }
}
