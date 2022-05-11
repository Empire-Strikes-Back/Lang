FROM ubuntu:20.04

RUN apt-get update && apt-get install sudo


WORKDIR /avatar
RUN git init && \
    git remote add origin https://github.com/TheBB/aligulac && \
    git fetch --depth 1 origin 4b18637a6efb898821f15beac9947551aca2a424 && \
    git checkout FETCH_HEAD