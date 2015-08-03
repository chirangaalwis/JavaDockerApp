package org.wso2.carbon.javadocker.tomcat.test;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import org.wso2.carbon.javadocker.tomcat.IApacheTomcatContainerHandler;
import org.wso2.carbon.javadocker.tomcat.implementation.ApacheTomcatContainerHandler;

import java.io.IOException;

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
 * a class to test the ApacheTomcatContainerHandler.stopTomcatContainer method
 */

public class StopContainerTest {
    public static void main(String[] args) {
        try {
            IApacheTomcatContainerHandler handler = new ApacheTomcatContainerHandler();
            handler.stopTomcatContainer("6632655efdd1");
        }
        catch(DockerCertificateException exception) {
            exception.printStackTrace();
        }
        catch(DockerException exception) {
            exception.printStackTrace();
        }
        catch(InterruptedException exception) {
            exception.printStackTrace();
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
    }
}
