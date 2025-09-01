# STMark

## Contents

- [Introduction](#introduction)
- [Features](#features)
- [PRELIMINARIES](#preliminaries)
- [Module Overview](#module overview)
- [Getting started](#getting started)

## Introduction

STMark is a zero-distortion robust database watermarking technique designed to provide copyright protection for datasets where any modification is strictly prohibited. It ensures that embedding watermarks does not affect the accuracy or availability of the data, making it suitable for high-accuracy applications such as governmental applications, medical applications, etc.

## Features

- ***Zero-distortion:*** PhiMark embeds watermarks by extracting features and generating a mask for each partition, without making any modifications to the original data,  preserving full availability.  To our knowledge, PhiMark is the first scheme to truly achieve zero-distortion robust database watermarking.
- ***Broader applicability:*** PhiMark does not depend on primary keys and is compatible with both textual and numerical data. Compared with traditional schemes, PhiMark is adapted to broader application scenarios.
- ***Automated parameter tuning:*** Unlike traditional robust watermarking schemes, PhiMark abandons the costly manual parameter tuning and introduces a genetic algorithm with a carefully designed fitness function, achieving efficient automated parameter adjustment.

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