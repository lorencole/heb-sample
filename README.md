# Read Me First
This is a sample REST API written per the spec provided.

It contains the following endpoints:


#### GET /images

• Returns HTTP 200 OK with a JSON response containing image details for all images including their metadata if available


#### GET /images?objects="dog,cat"

• Returns a HTTP 200 OK with a JSON response body containing image details for images that have been tagged with any of the objects listed


#### GET /images/{imageId}

• Returns HTTP 200 OK with a JSON response containing image details for the specified image


#### POST /images

• Posts a JSON request object which must include an base64 encoded image (no Data URI Scheme)  or URL. The request may also include an optional label for the image, and an optional field to enable object detection.
• Returns a HTTP 200 OK with a JSON response body including image details for the created image. 


#### Image details response object
<pre>
 {
   "id": "61942b004c9aaf7578bc2df2",
   "sourceUri": null,
   "metadata": {
     "[JPEG] Data Precision": "8 bits",
     "[JPEG] Image Height": "598 pixels",
     "[Huffman] Number of Tables": "4 Huffman tables",
     "[JFIF] Version": "1.1",
     "[JPEG] Component 1": "Y component: Quantization table 0, Sampling factors 2 horiz/2 vert",
     "[JPEG] Component 2": "Cb component: Quantization table 1, Sampling factors 1 horiz/1 vert",
     "[JPEG] Component 3": "Cr component: Quantization table 1, Sampling factors 1 horiz/1 vert",
     "[IPTC] Special Instructions": "FBMD0f000758010000a7090000e8180000451a00001b200000a42f0000ca40000040420000",
     "[JFIF] Y Resolution": "1 dot",
     "[File Type] Detected File Type Long Name": "Joint Photographic Experts Group",
     "[File Type] Expected File Name Extension": "jpg",
     "[JPEG] Number of Components": "3",
     "[JPEG] Image Width": "500 pixels",
     "[File Type] Detected File Type Name": "JPEG",
     "[JFIF] Thumbnail Width Pixels": "0",
     "[JFIF] X Resolution": "1 dot",
     "[File Type] Detected MIME Type": "image/jpeg",
     "[JPEG] Compression Type": "Baseline",
     "[JFIF] Thumbnail Height Pixels": "0",
     "[JFIF] Resolution Units": "none"
   },
   "tags": [
     "bird",
     "vertebrate",
     "art",
     "font"
   ],
   "label": "also labels exist"
 }
</pre>

To see example usage, please refer to REST_curl.txt
I've also included my Insomnia.json if which can be imported into an Insomnia REST client. Test images and base64 in src/test/resources.

# Getting Started

### GCP Vison
This application uses GCP vision to tag images. A GCP key is required to run these tests. For details on creating an API key please consult: [https://cloud.google.com/vision/docs/setup](https://cloud.google.com/vision/docs/setup). (Or email me, a key can be provided)

Configure your key in **docker-compose.override.yml** on line 8, GCP_KEY=<your key>

### How to run in docker
1. Install Docker and docker-compose
2. Build the app and explode the jar with
 
 `mvn -Dtest=\!GcpVisionTest package`
 
 `unzip -o target/heb-sample-0.0.1-SNAPSHOT.jar -d target/dependency`
 
3. Build and run with docker-compose 

 `sudo docker-compose build`
 
 `sudo docker-compose up`

### Setting up a dev environment
For development purposes we can start the mongo-db docker instance on it's own and attach to it from a java application run in our ide (instructions for Eclipse). To do so:

1. Run `sudo docker-compose -f docker-compose.yml -f docker-compose.dev.yml up` to start mongo-db, add the following line to **/etc/hosts**: "127.0.0.1 mongo-db"
2. Run the app from the command line with `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
3. Verify the server is working with `curl --request GET --url http://localhost:8080/hello`

### Running tests
Tests can be run from the command line with:

`mvn clean test`

The test suite includes an integration test for GCP Vision which requires a GCP API Key. 

Your GCP API key needs to be in **src/test/resources/application.yaml** under **GCP_KEY** for GcpVisionTest to pass.

To skip this test run
`mvn -Dtest=\!GcpVisionTest clean test`





