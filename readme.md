Plupload Java Runtime - Unlimited Sized Streamed Integrity Checked Resumeable Uploads
=====================================================================================

What is Plupload Java Runtime
-----------------------------
Plupload Java Runtime is an addition to plupload which lets people
upload files of unlimited size. All other technologies (Flash, HTML5,
HTML4) suffers when it comes to uploading large files. The only
possible cross browser means to upload large files is Java - for more
on this matter see [survey of large browser uploads](http://www.cabo.dk/blog/jakobs-blog/survey-of-large-browser-uploads).

What you need to build Plupload Java Runtime
--------------------------------------------
* Install Java JDK 1.6: [http://java.sun.com/javase/downloads/index.jsp](http://java.sun.com/javase/downloads/index.jsp)
* Install Apache Ant you can find it at: [http://ant.apache.org/](http://ant.apache.org/)
* Add Apache Ant to your systems path environment variable, this is not required but makes it easier to issue commands to Ant without having to type the full path for it.

How to build Plupload
----------------------

In the root directory of Plupload Java Runtime where the build.xml file is you can run ant against different targets.

`ant`

compiles, jars, and signs the runtime.

In order to read files from the users file system, the applet must be
signed. The ant target expects a plupload certificate. The certificate
is created with:

`keytool -genkey -alias plupload`

Note: the build file expects that the java key store password is 123456.

Running
-------
See [Plupload Backends](https://github.com/jakobadam/plupload-backends)
