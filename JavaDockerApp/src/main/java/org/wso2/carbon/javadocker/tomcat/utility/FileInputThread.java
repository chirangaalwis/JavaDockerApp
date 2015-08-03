package org.wso2.carbon.javadocker.tomcat.utility;

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

public class FileInputThread implements Runnable {
    private String fileName;
    private List<String> fileContent;

    public FileInputThread(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getFileContent() {
        if(fileContent != null) {
            return fileContent;
        }
        else {
            return new ArrayList<String>();
        }
    }

    public void run() {
        try {
            FileInput input = new FileInput();
            input.openFile(fileName);
            List<String> ids = input.readDataFromFile();
            input.closeFile();
            fileContent = ids;
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
