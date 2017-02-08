#ios_gps

How To Link to an Xcode Project?

In Xcode Build Settings for your project:

Add to Library Search Paths ( LIBRARY_SEARCH_PATHS ) $(SRCROOT)/../../../addons/ofxiOSBoost/libs/boost/lib/ios
Add to Header Search Paths ( HEADER_SEARCH_PATHS )
$(SRCROOT)/../../../addons/ofxiOSBoost/libs/boost/include
In Xcode for a Build Target select the Target under Build Phases

Add to 'Link Binary With Libraries' the libboost.a found in the ofxiOSBoost/libs/boost/lib/ios directory.
If not openFrameworks just add the libs/boost/include to Header Search Paths and the libs/boost/ios to Library Search Paths

Architectures in Pre-Build Library (Fat Lib)

See the other branches on this repository (All libc++ std=c11)

arm64 : (iOS 7, 8, 9 64bit only) [iPhone 5S, iPhone 6/6S, iPhone 6/6S Plus, iPad Air /2, iPad Mini Retina 1/2/3/4, iPad Pro]
armv7 : (iOS 5, 6, 7, 8, 9) [All devices]
i386 : (iOS Simulator iPad 2, 3, 4, iPhone 4S, 5, 5C)
x86_64: (iOS Simulator iPad Air, iPhone 5S, iPhone 6/6S, iPhone 6/6S Plus)