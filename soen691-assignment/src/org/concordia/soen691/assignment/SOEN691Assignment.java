package org.concordia.soen691.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.cli.*;

/*
 * @version 1.0
 * @since 1.0
 * <p>
 * This is the main class of the project. It loads the configuration from the properties file and executes the application accordingly.
 * The target source folder for the static code analyzer to use is configured in the properties file.
 * To parse only a single file the full path to the file needs to be provided in the command line.
 * </p>
 */

public class SOEN691Assignment {

	public static void main(String[] args) {

		String formatterMessage = "java -jar soen691-assignment.jar\n Or, java -jar soen691-assignment-0.0.1-jar-with-dependencies.jar";

		Options options = new Options();

        Option input = new Option("i", "input", true, "input-file");
        input.setRequired(false);
        options.addOption(input);

        Option config = new Option("c", "config", true, "config-file");
        config.setRequired(false);
        options.addOption(config);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd=null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(formatterMessage, options);

            System.exit(1);
        }

		try {
			if (cmd.hasOption("config")) {
				ConfigurationLoader.init(cmd.getOptionValue("config"));
			}
			else {
				ConfigurationLoader.init();
			}
		} catch (FileNotFoundException e) {
			printMissingConfigurationFileErrorMessage();
			System.exit(2);
		} catch (IOException e) {
			printParseConfigurationFileErrorMessage();
			e.printStackTrace();
			System.exit(1);
		} catch (URISyntaxException e) {
			printURISyntaxErrorMessage();
			e.printStackTrace();
			System.exit(3);
		}

        BugFinder bugFinder = new BugFinder();

		if (cmd.hasOption("input")) {
			try {
				System.out.println("\nUsing single file:" + cmd.getOptionValue("input"));
				File in = new File(cmd.getOptionValue("input"));
				bugFinder.parseOneFile(in);
				//in.close();
			} catch (FileNotFoundException e) {
				System.out.println("File: " + cmd.getOptionValue("input") + " not found!");
				printUsage();
				formatter.printHelp(formatterMessage, options);
				System.exit(1);
			}
		} else {
			System.out.println("Using DIRECTORY structure from configuration file");
			bugFinder.parseDirectoryStructure();
		}

		bugFinder.printResults();
	}

	private static void printParseConfigurationFileErrorMessage() {
		System.out.println("\n The was some exception processing the configuration file.");
	}

	private static void printMissingConfigurationFileErrorMessage() {
		System.out.println("\n The application requires a configuration.properties file in the same folder with the jar file. To specify a different location use the -c option.");
	}

	private static void printURISyntaxErrorMessage() {
		System.out.println("\n There was an error with the URI of the jar file location.");
	}

	static void printUsage() {
		System.out.println("\n The application may parse one file if provided as argument or by default parse a directory structure defined in the properties file");
	}

}
