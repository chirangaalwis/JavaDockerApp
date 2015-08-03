package org.wso2.carbon.javadocker.tomcat.test;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import org.wso2.carbon.javadocker.tomcat.IApacheTomcatContainerHandler;
import org.wso2.carbon.javadocker.tomcat.implementation.ApacheTomcatContainerHandler;

import java.io.IOException;

/**
 * Created by chiranga on 8/2/15.
 */
public class RemoveContainerTest {
    public static void main(String[] args) {
        try {
            IApacheTomcatContainerHandler handler = new ApacheTomcatContainerHandler();
            handler.removeTomcatContainer("34c9d5c42396");
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
