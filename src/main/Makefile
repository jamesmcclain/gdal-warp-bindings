JAVA ?= java
JAVAC ?= javac
SO ?= so
ARCH ?= amd64


.PHONY: tests clean cleaner cleanest

gdalwarp.jar: resources/libgdalwarp_bindings-$(ARCH).$(SO) java/com/azavea/gdal/GDALWarp.class java/cz/adamh/utils/NativeUtils.class
	(cd java ; jar -cvf ../$@ com/azavea/gdal/*.class cz/adamh/utils/*.class ../resources/*)

../libgdalwarp_bindings-$(ARCH).$(SO):
	$(MAKE) -C .. libgdalwarp_bindings-$(ARCH).$(SO)

resources/libgdalwarp_bindings-$(ARCH).$(SO): ../libgdalwarp_bindings-$(ARCH).$(SO)
	cp -f $< $@

java/com/azavea/gdal/GDALWarp.class java/cz/adamh/utils/NativeUtils.class: java/com/azavea/gdal/GDALWarp.java java/cz/adamh/utils/NativeUtils.java
	$(JAVAC) -h .. -cp java $<

GDALWarpThreadTest.class: GDALWarpThreadTest.java gdalwarp.jar
	$(JAVAC) -cp gdalwarp.jar $<

GDALWarpMetadataTest.class: GDALWarpMetadataTest.java gdalwarp.jar
	$(JAVAC) -cp gdalwarp.jar $<

# MIT-Licensed code (that license is compatible with Apache 2.0)
java/cz/adamh/utils/NativeUtils.java:
	wget -k "https://raw.githubusercontent.com/adamheinrich/native-utils/e6a39489662846a77504634b6fafa4995ede3b1d/src/main/$@" -O $@

../experiments/data/c41078a1.tif:
	$(MAKE) -C ../experiments data/c41078a1.tif

tests: gdalwarp.jar GDALWarpThreadTest.class ../experiments/data/c41078a1.tif ../experiments/data/gdal-uint8raw-test.tiff
	$(JAVA) -cp gdalwarp.jar:. GDALWarpThreadTest ../experiments/data/c41078a1.tif ../experiments/data/gdal-uint8raw-test.tiff small
	GDALWARP_GC_LOCK=1 $(JAVA) -cp gdalwarp.jar:. GDALWarpThreadTest ../experiments/data/c41078a1.tif ../experiments/data/gdal-uint8raw-test.tiff small

clean:
	rm -f *.class java/cz/adamh/utils/*.class java/com/azavea/gdal/*.class

cleaner: clean
	rm -f gdalwarp.jar
	rm -f resources/libgdalwarp_bindings-$(ARCH).$(SO)

cleanest: cleaner
