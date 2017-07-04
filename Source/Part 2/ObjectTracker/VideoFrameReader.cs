using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Accord.Video.FFMPEG;

namespace ObjectTracker
{
    public class VideoFrameReader : IDisposable
    {
        public readonly VideoFileReader UnderlyingReader;

        public VideoFrameReader(string filePath)
        {
            UnderlyingReader = new VideoFileReader();
            UnderlyingReader.Open(filePath);
        }

        /// <summary>
        /// Processes all frames of a video
        /// </summary>
        /// <param name="onFrameProcessed">Provides a bitmap and an index of a frame that is processing</param>
        public void ReadAllFrames(Action<Bitmap, int> onFrameProcessed)
        {
            Bitmap frameBitmap;
            int index = 0;
            while ((frameBitmap = UnderlyingReader.ReadVideoFrame()) != null)
            {
                onFrameProcessed.Invoke(frameBitmap, index);
                index++;
            }
        }

        public void Dispose()
        {
            UnderlyingReader.Dispose();
        }
    }
}
