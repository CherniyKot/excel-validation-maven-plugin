package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.sun.javafx.scene.traversal.ContainerTabOrder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static org.example.GoogleUtil.APPLICATION_NAME;
import static org.example.GoogleUtil.JSON_FACTORY;
import static org.example.GoogleUtil.getCredentials;


@Mojo(name = "check", defaultPhase = LifecyclePhase.VALIDATE)
public class ExcelCheck extends AbstractMojo {
    @Parameter(property = "spreadsheetId")
    private String spreadsheetId;
    @Parameter(property = "conditions")
    private List<Condition> conditions;
    
    private boolean error = false;
    
    public void execute() throws MojoExecutionException {
        // Build a new authorized API client service.
        NetHttpTransport HTTP_TRANSPORT = null;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        }
        catch (Exception e) {
            throw new MojoExecutionException("Something went wrong with Internet connection", e);
        }
        
        //final String spreadsheetId = "1Ry739nTeDgEDXIxVIJZG8SUlKCKeohxOHn3AKsIxJ_w";
        //final String range = "Поток 1.6!A12";
        Sheets service = null;
        
        try {
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        catch (IOException e) {
            throw new MojoExecutionException("Something went wrong in your life", e);
        }
        
        for (Condition condition : conditions) {
            ValueRange response = null;
            try {
                response = service.spreadsheets().values()
                        .get(spreadsheetId, condition.cell)
                        .execute();
            }
            catch (IOException e) {
                throw new MojoExecutionException("Wrong Cell Address", e);
            }
            
            List<List<Object>> value = response.getValues();
            if (value == null) {
                if(!condition.condition.toLowerCase().equals("empty"))
                {
                    error = true;
                    System.out.printf("[ERROR] Cell %s is empty%n", condition.cell);
                }
                //throw new MojoExecutionException("No data found in the spreadsheet");
            }
            else {
                condition.condition = condition.condition.toLowerCase();
                String t = value.get(0).get(0).toString();
                switch (condition.condition) {
                    case "equal":
                        if (!t.equals(condition.value)) {
                            error = true;
                            System.out.printf("[ERROR] Cell %s equals to %s, not to %s%n", condition.cell, t, condition.value);
                        }
                        break;
                    case "not_equal":
                        if (t.equals(condition.value)) {
                            error = true;
                            System.out.printf("[ERROR] Cell %s equals to %s%n", condition.cell, condition.value);
                        }
                        break;
                    case "less":
                    case "not_greater_equal":
                        if (Integer.parseInt(condition.value)<=Integer.parseInt(t)) {
                            error = true;
                            System.out.printf("[ERROR] Cell %s is not less than %s%n", condition.cell, condition.value);
                        }
                        break;
                    case "greater":
                    case "not_less_equal":
                        if (Integer.parseInt(condition.value)>=Integer.parseInt(t)) {
                            error = true;
                            System.out.printf("[ERROR] Cell %s is not greater than %s%n", condition.cell, condition.value);
                        }
                        break;
                    case "less_equal":
                    case "not_greater":
                        if (Integer.parseInt(condition.value)<Integer.parseInt(t)) {
                            error = true;
                            System.out.printf("[ERROR] Cell %s is greater than %s%n", condition.cell, condition.value);
                        }
                        break;
                    case "greater_equal":
                    case "not_less":
                        if (Integer.parseInt(condition.value)>Integer.parseInt(t)) {
                            error = true;
                            System.out.printf("[ERROR] Cell %s is less than %s%n", condition.cell, condition.value);
                        }
                        break;
                    case "empty":
                        if(!t.isEmpty())
                        {
                            error = true;
                            System.out.printf("[ERROR] Cell %s is not empty%n", condition.cell);
                        }
                        break;
                    case "not_empty":
                        if(t.isEmpty())
                        {
                            error = true;
                            System.out.printf("[ERROR] Cell %s is empty%n", condition.cell);
                        }
                        break;
                    default:
                        System.out.printf("[WARNING] Unknown condition \"%s\"%n", condition.condition);
                }
            }
        }
        if(error){
            throw new MojoExecutionException("Excel validation is not passed");
        }
        
    }
}
