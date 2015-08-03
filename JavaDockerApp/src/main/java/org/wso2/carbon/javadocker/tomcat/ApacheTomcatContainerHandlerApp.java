package org.wso2.carbon.javadocker.tomcat;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import org.wso2.carbon.javadocker.tomcat.implementation.ApacheTomcatContainerHandler;

import java.io.IOException;
import java.util.Scanner;

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
public class ApacheTomcatContainerHandlerApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            final ApacheTomcatContainerHandler handler = new ApacheTomcatContainerHandler();
            int input;
            do {
                displayMainMenu();
                input = captureUserInput();
            }
            while((input < 1) || (input > 7));

            processUserInput(input, handler);
        }
        catch (DockerCertificateException exception) {
            System.exit(1);
        }
        catch(DockerException exception) {
            System.exit(1);
        }
        catch(IOException exception) {
            System.exit(1);
        }
        catch(InterruptedException exception) {
            System.exit(1);
        }
    }

    private static void displayMainMenu() {
        String menuContent = "***WELCOME TO THE APACHE TOMCAT DOCKER CONTAINER HANDLER***\n\n"
                + "1 - Create new Docker Tomcat Container\n"
                + "2 - Start existing Docker Tomcat Container(ID specified)\n"
                + "3 - Start existing Docker Tomcat Container\n"
                + "4 - Stop existing Docker Tomcat Container\n"
                + "5 - Kill existing Docker Tomcat Container\n"
                + "6 - Remove existing Docker Tomcat Container\n"
                + "7 - Exit\n"
                + "Enter the number of your choice out of the above: ";

        System.out.print(menuContent);
    }

    private static int captureUserInput() {
        int userInput = -1;

        int temp = scanner.nextInt();
        System.out.println();
        if(temp >= 1 && temp <= 7) {
            userInput = temp;
        }

        return userInput;
    }

    private static void processUserInput(int userInput, ApacheTomcatContainerHandler handler) throws DockerException, InterruptedException {
        String id;

        switch (userInput) {
        case 1:
            handler.createTomcatContainer();
            break;
        case 2:
            System.out.print("Specify the ID: ");
            id = scanner.next();
            System.out.println();
            handler.startTomcatContainer(id);
            break;
        case 3:
            handler.startTomcatContainer();
            break;
        case 4:
            System.out.print("Specify the ID: ");
            id = scanner.next();
            System.out.println();
            handler.stopTomcatContainer(id);
            break;
        case 5:
            System.out.print("Specify the ID: ");
            id = scanner.next();
            System.out.println();
            handler.killTomcatContainer(id);
            break;
        case 6:
            System.out.print("Specify the ID: ");
            id = scanner.next();
            System.out.println();
            handler.removeTomcatContainer(id);
            break;
        case 7:
            System.exit(0);
            break;
        }
    }
}
