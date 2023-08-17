# BUMBLE - Persistency

This is a master project part of 

[BUMBLE]: https://itea4.org/project/bumble.html

project. This work elaborates on the solution and its development for enabling asynchronous modeling capabilities of the BUMBLE-CE project, consisting of several Eclipse-based server-side plug-ins running in the EMF.cloud model server including Model Inventory Controller, Persistency Controller and three Persistency Drivers. The Model Inventory Controller is responsible for 1) monitoring the creation and deletion of collaboration sessions in the Model Inventory, 2) parsing the monitored JSON patch for creating or deleting collaboration sessions and obtaining the required information in the Model Inventory (e.g. model name and location), and 3) Call the Persistency Controller to synchronize and persist the model. The Persistency Controller provides a model synchronization and persistence class that can be extended, and other plug-ins linked to it can implement specific synchronization and persistence methods based on the persistence platform through inheritance. Three example persistence drivers are created in this project, namely Git Driver, MongoDB Driver and Local File System Driver, to demonstrate the effect when they are called. In addition, the persistence controller also provides an API interface to users, so that users can directly use the model synchronization and persistence functions as needed.

![The architecture of the BUMBLE-CE and the highlighted focuses of this thesis project. (Taken from BUMBLE Deliver D5.1)](/Users/ice/Desktop/VU-BUMBLE-Master-Project/Figures/BUMBLE_Architecture.png)

![The architecture of this project.](/Users/ice/Desktop/VU-BUMBLE-Master-Project/Figures/Architecture.png)

## Prerequisites

The following libraries/softwares need to be installed on your system:

| Name                                                         | Version          |
| ------------------------------------------------------------ | ---------------- |
| [Eclipse Modeling Tools](https://projects.eclipse.org/projects/modeling.emf.emf) | 2022-09 (4.25.0) |
| [Java](https://www.oracle.com/java/technologies/downloads/#java11) | 11 & 17          |
| [JGit](https://projects.eclipse.org/projects/technology.jgit/downloads) | 6.0.5            |

## Dependent Project

To run this project, the following project need to be downloaded and installed:

| Project                           | GitHub Link                                                |
| --------------------------------- | ---------------------------------------------------------- |
| EMF.Cloud Model Server (modified) | https://github.com/ZhaolinFang/emfcloud-modelserver-BUMBLE |

Please follow the README in the above repository to build and run the EMF.Cloud Model Server.

## Run

