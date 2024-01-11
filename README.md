# OpenDataHub AOM Parser

# Table of Contents
1. [Introduction](#introduction)
2. [Schema Parser](#schema-parser)
3. [Data Parser](#data-parser)
4. [Interaction with the Data](#interaction-with-the-data)

# Introduction
This repository aims to facilitate the interaction with the OpenDataHub API by providing a structured approach to handle
the data. The following parsers and tools are completely written in Java. This project can be used as a library to
enhance the Developer Experience and to provide a flexible model of OpenDataHub's data. The schema updates automatically
as soon as the API is updated since the model is persisted in runtime.

# Schema Parser
This repository provides a parser for the [OpenDataHub](https://opendatahub.bz.it/) API to be able to be mapped to the
Adaptive Object Model. This allows to interact with the API in a more convenient way using the Entity and Property 
classes provided by the AOM. As a basis, the swagger.json file provided by the API is used. The parser is able to build
AOM Type classes based on the given schema components and construct relationships between them. In Addition, the parsing
of Lists and HashMaps (components that have the "additionalProperties" property) is supported.

# Data Parser
Despite the schema parser, this repository also provides a parser for the data provided by the OpenDataHub API. The data
can be fetched using the provided API endpoints and directly be parsed into AOM objects to retrain the appropriate Entity
classes.

# Interaction with the Data
To interact with the data, a CLI utility tool is offered that allows to fetch the data from the API, parse it into the 
AOM and browse the data using CLI commands.
