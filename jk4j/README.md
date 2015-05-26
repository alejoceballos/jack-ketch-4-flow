# **Jack Ketch for Java**

The flow executor for Java projects.

**NOTE:** 
+ This document was created on **April 26, 2015**. Last update was on ***April 27, 2015***.

## [ Goal ]

Make [jk4flow](https://github.com/alejoceballos/jack-ketch-4-flow "Jack Ketch 4 Flow") project in Java a **reality**!

# Subprojects

## Workflow

Responsible for all workflow domain model.

In this project you may find all classes responsible for representing an Activity Model Diagram. Since we declare pure OO classes in Java, all entities such as Actions, Decisions, Forks and so on are concrete implementations of node's abstractions.



## Engine

Responsible for executing the workflow model

## Transformation

Responsible for reading a JSON activity diagram and creating an workflow instance.

**NOTE:** *It is this project intention to implement XML translators since most of third-party visual modelers save their diagrams in XMI format.*