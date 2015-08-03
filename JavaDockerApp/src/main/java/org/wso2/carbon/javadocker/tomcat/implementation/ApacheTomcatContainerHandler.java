package org.wso2.carbon.javadocker.tomcat.implementation;

import com.spotify.docker.client.*;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import org.wso2.carbon.javadocker.tomcat.IApacheTomcatContainerHandler;
import org.wso2.carbon.javadocker.tomcat.utility.FileInputThread;
import org.wso2.carbon.javadocker.tomcat.utility.FileOutputThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/**
 * a class which implements the IApacheTomcatContainerHandler Java interface
 */
public class ApacheTomcatContainerHandler implements IApacheTomcatContainerHandler {

    // list of already created Docker Container IDs
    private List<String> containerIDs;
    // constant Docker Client to hold a client based on DOCKER_HOST and DOCKER_CERT_PATH env vars
    private final DockerClient DOCKER_CLIENT;
    // String constant depicting the name of the text file's name to which IDs are saved
    private static final String FILE_NAME = "tomcat_containers.txt";

    /**
     * a parameter less ApacheTomcatContainerHandler constructor which initializes the class scope
     * fields of the ApacheTomcatContainerHandler class
     * @throws DockerCertificateException
     * @throws IOException
     */
    public ApacheTomcatContainerHandler() throws DockerCertificateException, IOException {
        containerIDs = readIDsFromFile();
        DOCKER_CLIENT = DefaultDockerClient.fromEnv().build();
    }

    /**
     * creates a Docker Container which uses the Apache Tomcat 8.0 Docker Image
     * @throws DockerException
     * @throws InterruptedException
     */
    public void createTomcatContainer() throws DockerException, InterruptedException {
        // Create a container based on the configurations
        final ContainerConfig configuration = ContainerConfig.builder().image("tomcat:8.0").build();
        final ContainerCreation creation = DOCKER_CLIENT.createContainer(configuration);

        /*
        adds container ids to the list and updates the secondary storage text file holding the
        id data
        */
        containerIDs.add(creation.id());
        writeIDsToFile();
    }

    /**
     * starts a Apache Tomcat Docker Container by using the existing Tomcat Containers created
     * without needing to provide an existing Container ID as a parameter
     * @throws DockerException
     * @throws InterruptedException
     */
    public void startTomcatContainer() throws DockerException, InterruptedException {
        String staticContainerID = getStaticContainerID();
        if(staticContainerID !=  null) {
            DOCKER_CLIENT.startContainer(staticContainerID);
        }
    }

    /**
     * starts the Docker Container specified by the Container ID provided as a parameter
     * @param id Docker Container ID of the Container to be started
     * @throws DockerException
     * @throws InterruptedException
     */
    public void startTomcatContainer(String id) throws DockerException, InterruptedException {
        if(id != null) {
            DOCKER_CLIENT.startContainer(id);
        }
    }

    /**
     * stops the Docker Container specified by the Container ID provided as a parameter
     * @param id Docker Container ID of the Container to be stopped
     * @throws DockerException
     * @throws InterruptedException
     */
    public void stopTomcatContainer(String id) throws DockerException, InterruptedException {
        if(id != null) {
            DOCKER_CLIENT.stopContainer(id, 30);
        }
    }

    /**
     * kills the Docker Container specified by the Container ID provided as a parameter
     * @param id Docker Container ID of the Container to be killed
     * @throws DockerException
     * @throws InterruptedException
     */
    public void killTomcatContainer(String id) throws DockerException, InterruptedException {
        if(id != null) {
            DOCKER_CLIENT.killContainer(id);
        }
    }

    /**
     * removes the Docker Container specified by the Container ID provided as a parameter
     * @param id Docker Container ID of the Container to be removed
     * @throws DockerException
     * @throws InterruptedException
     */
    public void removeTomcatContainer(String id) throws DockerException, InterruptedException {
        if(id != null) {
            // removes the Container specified by the ID
            DOCKER_CLIENT.removeContainer(id);
            /*
            removes the specified container ID from the list and updates the secondary
            storage text file holding the id data
             */
            for(int count = 0 ; count < containerIDs.size() ; count++) {
                if(containerIDs.get(count).contains(id)) {
                    containerIDs.remove(count);
                }
            }
            writeIDsToFile();
        }
    }

    /**
     * checks if the Apache Tomcat Docker Container specified by the String ID parameter has already
     * being created and exists, returns true if exists else false
     * @param id Docker Container ID of the Container to be checked for existence
     * @return returns true if Docker Container exists else false
     */
    public boolean checkContainerExistence(String id) {
        if(id != null) {
            for(String containerId : containerIDs) {
                if(containerId.equals(id)) {
                    return true;
                }
            }
        }
        else {
            return false;
        }
        return false;
    }

    /**
     * private, utility method which returns the first available non-running Apache Tomcat Docker
     * Container's ID
     * @return first available, static Apache Tomcat Docker Container ID from the list of Containers
     *         already created
     * @throws DockerException
     * @throws InterruptedException
     */
    private String getStaticContainerID() throws DockerException, InterruptedException {
        List<String> runningContainerIDs = getRunningContainerIDs();
        for(String existingContainerID : containerIDs) {
            if(!runningContainerIDs.contains(existingContainerID)) {
                return existingContainerID;
            }
        }
        return null;
    }

    /**
     * private, utility method which returns a String list of currently running Docker Container IDs
     * @return a String list of currently running Docker Container IDs
     * @throws DockerException
     * @throws InterruptedException
     */
    private List<String> getRunningContainerIDs() throws DockerException, InterruptedException {
        List<String> containerIDs = new ArrayList<String>();
        for(Container container : DOCKER_CLIENT.listContainers()) {
            containerIDs.add(container.id());
        }

        return containerIDs;
    }

    /**
     * writes the existing Docker Container IDs to the external text file which holds the Docker
     * Container IDs
     */
    private void writeIDsToFile() {
        FileOutputThread outputThread = new FileOutputThread(FILE_NAME, containerIDs);
        outputThread.run();
    }

    /**
     * reads the existing Apache Tomcat Docker Container IDs created using this application from
     * the text file
     * @return the existing Apache Tomcat Docker Container IDs
     * @throws IOException
     */
    private List<String> readIDsFromFile() throws IOException {
        FileInputThread inputThread = new FileInputThread(FILE_NAME);
        inputThread.run();
        return inputThread.getFileContent();
    }

    // TODO: check for a better alternative to identify the name of the Docker Image from the id
    // TODO: check why diamond types are not supported at this language level when using ArrayList
    // TODO: *** Learn JSON ***
}
