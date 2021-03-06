﻿using System;
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
        private const string OUTPUT_VIDEO_NAME = "Walk1.mpg";

        static void Main(string[] args)
        {
            var objectTracker = new VideoObjectTracker();
            objectTracker.Process(INPUT_VIDEO_PATH, OUTPUT_DIRECTORY, OUTPUT_VIDEO_NAME);
        }
    }
}
