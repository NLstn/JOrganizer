package com.nlstn.jmediaOrganizer.properties;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nlstn.jmediaOrganizer.JMediaOrganizer;

/**
 * Creation: 23.12.2017
 *
 * @author Niklas Lahnstein
 */
public class LaunchConfiguration {

	private static Logger log;

	static {
		log = LogManager.getLogger(LaunchConfiguration.class);
	}

	public static LaunchConfiguration parse(String[] args) {
		return new LaunchConfiguration(args);
	}

	private CommandLine		cmd;
	private Options			options;
	private HelpFormatter	help;

	private LaunchConfiguration(String[] args) {
		log.trace("Building CommandLineParser");
		options = new Options();

		Option headless = Option.builder("h").desc("Run in headless mode").longOpt("headless").build();
		headless.setRequired(false);
		options.addOption(headless);

		Option input = Option.builder("i").hasArg().desc("The folder to convert").longOpt("inputFolder").build();
		options.addOption(input);

		Option invalidTypes = Option.builder("t").hasArg().desc("Types to delete").longOpt("invalidTypes").build();
		options.addOption(invalidTypes);

		Option id3ToNameEnabled = Option.builder("id3").hasArg().desc("Enable ID3ToNameConverter").longOpt("id3ToNameEnabled").build();
		options.addOption(id3ToNameEnabled);

		Option id3ToNamePattern = Option.builder("id3p").hasArg().desc("ID3ToNameConverter Pattern").longOpt("id3ToNamePattern").build();
		options.addOption(id3ToNamePattern);

		Option outputFolder = Option.builder("out").hasArg().desc("Output Folder").longOpt("outputFolder").build();
		options.addOption(outputFolder);

		Option threadCount = Option.builder("tc").hasArg().desc("Thread Count for Conversion").longOpt("threadCount").build();
		options.addOption(threadCount);

		CommandLineParser parser = new DefaultParser();
		help = new HelpFormatter();

		try {
			cmd = parser.parse(options, args);
		}
		catch (ParseException e) {
			System.out.println(e.getMessage());
			help.printHelp("JMediaOrganizer", options);
			System.exit(1);
		}
		processArgs();
	}

	private void processArgs() {
		// If headless mode is enabled, input folder has to be supplied via command args
		if (cmd.hasOption("h") && !cmd.hasOption("i")) {
			String headlessOption = cmd.getOptionValue("h");
			if (Boolean.valueOf(headlessOption)) {
				System.out.println("You need to specify an input folder (-i), if you run in headless mode!");
				help.printHelp("JMediaOrganizer", options);
				System.exit(1);
			}
		}
		if (cmd.hasOption("i")) {
			String inputFolder = cmd.getOptionValue("i");
			File f = new File(inputFolder);
			if (!(f.exists() && f.isDirectory())) {
				log.error("Invalid input folder!");
				help.printHelp("JMediaOrganizer", options);
			}
			else {
				JMediaOrganizer.setInputFolder(f);
				log.debug("Setting {} as the input folder", f.getAbsolutePath());
			}
		}
		if (cmd.hasOption("id3")) {
			boolean id3ToNameEnabled = Boolean.valueOf(cmd.getOptionValue("id3"));
			Settings.setID3ToNameEnabled(id3ToNameEnabled);
			log.debug("Setting id3ToNameEnabled setting to {}", id3ToNameEnabled);
		}
		if (cmd.hasOption("tc")) {
			try {
				int threadCount = Integer.parseInt(cmd.getOptionValue("tc"));
				int cores = Runtime.getRuntime().availableProcessors();
				if (threadCount <= 0 || threadCount > cores) {
					log.error("Invalid thread count! {}", threadCount);
				}
				else {
					Settings.setThreadCount(threadCount);
				}
			}
			catch (NumberFormatException e) {
				log.error("Threadcount must be a number! {}", cmd.getOptionValue("tc"));
			}
		}
		if (cmd.hasOption("id3p")) {
			Settings.setID3ToNamePattern(cmd.getOptionValue("id3p"));
		}
		if (cmd.hasOption("out")) {
			String outPath = cmd.getOptionValue("out");
			File outFile = new File(outPath);
			if (!outFile.isDirectory()) {
				log.error("Output Folder {} is not a folder!", outFile.getAbsolutePath());
			}
			else {
				Settings.setOutputFolder(outFile.getAbsolutePath());
			}
		}
		if (cmd.hasOption("t")) {
			String invalidTypes = cmd.getOptionValue("t");
			List<String> typeList = Arrays.asList(invalidTypes.split(";"));
			Settings.setInvalidTypes(typeList);
		}
	}

	public boolean isHeadlessModeEnabled() {
		return cmd.getOptionValue("h") == "true";
	}
}