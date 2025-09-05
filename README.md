# STMark

## Contents

- [Introduction](#introduction)
- [Features](#features)
- [PRELIMINARIES](#preliminaries)
- [Module Overview](#module overview)
- [Getting started](#getting started)

## Introduction

STMark is a zero-distortion robust database watermarking technique designed to provide copyright protection for datasets where any modification is strictly prohibited. It ensures that embedding watermarks does not affect the accuracy or availability of the data, making it suitable for high-accuracy applications such as governmental applications, medical applications, etc.   It overcomes the reliance of existing watermarking schemes on specific data characteristics, such as structural characteristics or distribution characteristics, extending its applicability to all scalar and textual (ST) data.

## Features

- ***Broader applicability:*** STMark does not depend on primary keys and is compatible to all ST data. Compared with traditional schemes, STMark is adapted to broader application scenarios. To our knowledge, STMark is the first scheme to achieve such applicability.
- ***Zero-distortion:*** STMark embeds watermarks by extracting features and generating a mask for each partition, without making any modifications to the original data,  preserving full availability.  To our knowledge, STMark is the first scheme to truly achieve zero-distortion robust watermarking for ST data.
- ***Automated parameter tuning:*** Unlike traditional robust watermarking schemes, STMark abandons the costly manual parameter tuning and designs a heuristic parameter generation method by identifying intrinsic relationships among parameters and dataset characteristics.

## PRELIMINARIES

Before using this project, ensure that your environment meets the following requirements:

- **JDK11** or higher version

  - You can check your JDK version by running the following command:

    ```bash
     java -version
    ```

    If JDK 11 is not installed, download it from [here](https://www.oracle.com/java/technologies/downloads/#java11).

- **Maven3.9.6** or higher version

  - You can check your Maven version by running the following command:

    ```bash
     mvn -v
    ```

    If Maven is not installed, download and install it from the official [Maven website](https://maven.apache.org/download.cgi).

## Module overview

1. **Data.java:** Read datasets from CSV files and store the data along with relevant information. The dataset is read from a CSV file for simplicity, but any readable data format, such as SQL or other similar formats, can be used.
2. **Util.java:** a utility class that contains some general methods.
3. **watermark.java:** Including the core algorithms for watermark embedding and extraction.
4. **attack.java:** Simulating common malicious attacks on the dataset.
5. **Merge.java**: Preparing the test dataset.
6. **test.java**:Conducting experiments.



## Getting started

###### Maven configuration

To set up the project, ensure that your `pom.xml` file includes the necessary dependencies.  You can add the following dependencies in your `pom.xml`.



```pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>watermark</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>watermark</name>
    <url>http://maven.apache.org</url>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>

        <!-- OpenCSV: A library for reading and writing CSV files in Java -->
        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>5.0</version>
        </dependency>
        <!-- Jenetics: A Java genetic algorithm library -->
        <dependency>
            <groupId>io.jenetics</groupId>
            <artifactId>jenetics</artifactId>
            <version>4.3.0</version>
        </dependency>
        <!-- MySQL JDBC Driver: Used for connecting the application to a MySQL database -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.21</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>

        <!-- FastJSON: A high-performance JSON parser and serializer for Java -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.47</version>
        </dependency>
        
         <!-- The following dependencies are only required when testing on text-based datasets. -->
        <dependency>
            <groupId>net.sf.jwnl</groupId>
            <artifactId>jwnl</artifactId>
            <version>1.4.1-rc2</version>
        </dependency>

       <dependency>
            <groupId>com.example</groupId>
            <artifactId>ws4j</artifactId>
            <version>1.0.1</version>
       </dependency>

       <dependency>
            <groupId>org.apache.opennlp</groupId>
            <artifactId>opennlp-tools</artifactId>
            <version>2.1.0</version>
       </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

Since the Maven repository does not include `jwnl` and `ws4j`, you need to install them manually.  
We provide the required JAR files: `jwnl.jar` and `ws4j-1.0.1.jar`.  
After downloading them, follow the commands below in the project directory to install them:

```
mvn install:install-file -DgroupId="net.sf.jwnl" -DartifactId="jwnl" -Dversion="1.4.1-rc2" -Dpackaging="jar" -Dfile="yourpath\jwnl.jar"
```

```
mvn install:install-file -DgroupId=“com.example” -DartifactId=“ws4j” -Dversion=“1.0.1” -Dpackaging=“jar” -Dfile="yourpath\ws4j-1.0.1.jar"
```



###### Dataset preparation

Due to GitHub's 25MB limit for individual files, we have split the dataset during upload. Before running the project, the datasets need to be merged and placed in the `dataset` folder. The dataset merging script is located at `src\main\java\Merge\merge.java`.

![image-20250905102447787](img\1.png)





###### Running the Project

You can run the experiments you want to test in `src\main\java\test.java`.（Because the operation of Synonym substitution is rather time-consuming, Synonym substitution attacks take a long time）

![image-20250905102759014](img\2.png)



