/**
 * 
  Package: MAG - VistA Imaging
  WARNING: Per VHA Directive 2004-038, this routine should not be modified.
  Date Created:
  Site Name:  Washington OI Field Office, Silver Spring, MD
  Developer:
  Description: 

        ;; +--------------------------------------------------------------------+
        ;; Property of the US Government.
        ;; No permission to copy or redistribute this software is given.
        ;; Use of unreleased versions of this software requires the user
        ;;  to execute a written test agreement with the VistA Imaging
        ;;  Development Office of the Department of Veterans Affairs,
        ;;  telephone (301) 734-0100.
        ;;
        ;; The Food and Drug Administration classifies this software as
        ;; a Class II medical device.  As such, it may not be changed
        ;; in any way.  Modifications to this software may result in an
        ;; adulterated medical device under 21CFR820, the use of which
        ;; is considered to be a violation of US Federal Statutes.
        ;; +--------------------------------------------------------------------+

 */
package va.gov.vista.kidsassembler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

import va.gov.vista.kidsassembler.components.Routine;
import va.gov.vista.kidsassembler.components.KernelComponent;
import va.gov.vista.kidsassembler.manifest.KidsManifest;
import va.gov.vista.kidsassembler.manifest.ManifestComponent;
import va.gov.vista.kidsassembler.parsers.ComponentParser;
import va.gov.vista.kidsassembler.parsers.ComponentParserFactory;
import va.gov.vista.kidsassembler.writers.KidsWriter;

@SuppressWarnings("static-access")
public class KidsAssembler {
	private static final Logger logger = Logger.getLogger(KidsAssembler.class);
	private static final String PROPERTY_PREFIX = "va.gov.vista.kids.";
	private static final Options commandLineOptions = new Options();
	private static final Path configPath;
	private static final Path log4jConfigPath;

	static {
		Path appPath = null;
		try {
			appPath = Paths.get(KidsAssembler.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			logger.error(e);
		}

		configPath = appPath.resolve("../KIDSAssembler.properties");
		log4jConfigPath = appPath.resolve("../log4j.properties");

		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "manifest").hasArg()
				.withDescription("use given file for the build manifest")
				.isRequired().create("m"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "testVersion").hasArg()
				.withDescription("sets the test version").create("t"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "buildIEN").hasArg()
				.withDescription("sets the unique IEN of this build")
				.create("b"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "buildLabel").hasArg()
				.withDescription("sets the build label").create("l"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "outputDirectory").hasArg()
				.withDescription("sets the directory for the output")
				.create("o"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "outputFileName").hasArg()
				.withDescription("sets the output file name").create("n"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "startingKernelIen").hasArg()
				.withDescription("sets the starting kernel IEN").create("k"));
		commandLineOptions.addOption(OptionBuilder
				.withArgName(PROPERTY_PREFIX + "[property]=value").hasArgs(2)
				.withValueSeparator()
				.withDescription("use value for given property").create("D"));
	}

	public static Configuration loadConfiguration(CommandLine cl) {
		PropertiesConfiguration config = new PropertiesConfiguration();

		// set the configuration defaults
		config.setListDelimiter(',');
		try {
			config.load(configPath.toFile());
		} catch (ConfigurationException e) {
			logger.error(e);
		}

		// convert some shortcut names to the appropriate prefixes
		String value, clKey, configKey;
		for (Option option : cl.getOptions()) {
			clKey = option.getOpt();
			if (clKey != null && !clKey.equals("D")) {
				configKey = option.getArgName();
				value = cl.getOptionValue(clKey);
				if (value != null && !value.equals("")) {
					logger.debug("overriding property " + configKey
							+ " from the command line option " + clKey
							+ " with the value: " + value);
					config.setProperty(configKey, value);
				}
			}
		}

		// Override any properties specified on the command line
		Properties cliProps = cl.getOptionProperties("D");
		for (String propName : cliProps.stringPropertyNames()) {
			value = cliProps.getProperty(propName);
			logger.debug("overriding property " + propName
					+ " from the command line with the value: " + value);
			config.setProperty(propName, cliProps.getProperty(propName));
		}

		return config;
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure(log4jConfigPath.toAbsolutePath()
				.toString());
		CommandLine cl = parseCommandLine(args);
		Configuration config = loadConfiguration(cl);
		KidsAssembler assembler = new KidsAssembler(config);
		assembler.configure();
		assembler.loadManifest();
		assembler.loadKidsAttributes();
		assembler.loadKidsRequiredBuilds();
		assembler.verifySetup();
		assembler.loadComponents();
		assembler.renumberKernelComponents();
		assembler.writeKids();
	}

	public static CommandLine parseCommandLine(String[] args) {
		CommandLineParser parser = new GnuParser();
		CommandLine cl = null;
		try {
			cl = parser.parse(commandLineOptions, args);
		} catch (MissingOptionException moe) {
			logger.error(moe);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("KIDSAssembler", commandLineOptions);
			System.exit(0);
		} catch (UnrecognizedOptionException uroe) {
			logger.error(uroe);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("KIDSAssembler", commandLineOptions);
			System.exit(0);
		} catch (ParseException pe) {
			logger.error(pe);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("KIDSAssembler", commandLineOptions);
			System.exit(0);
		}
		return cl;
	}

	private final Configuration configuration;
	private final KidsPackage kidsPackage;
	private KidsManifest manifest;
	private Path manifestPath;
	private Path outputDirPath;
	private Path outputFilePath;
	private Path outputLogPath;
	private Path componentRootPath;
	private List<Path> searchPaths;
	private int startingKernelIen;

	public KidsAssembler(Configuration config) {
		kidsPackage = new KidsPackage();
		this.configuration = config;
	}

	public void configure() {
		logger.info("Configured Properties:");
		Iterator<String> keys = configuration.getKeys();
		String key;
		while (keys.hasNext()) {
			key = keys.next();
			logger.info(key + "=" + configuration.getProperty(key));
		}

		this.kidsPackage.setBuildIEN(configuration.getInt(PROPERTY_PREFIX
				+ "buildIEN", 1));
		this.kidsPackage.setTestVersion(configuration.getInt(PROPERTY_PREFIX
				+ "testVersion", 0));
		this.kidsPackage.setBuildLabel(configuration.getString(PROPERTY_PREFIX
				+ "buildLabel", ""));
		this.kidsPackage.setAlphaBetaTesting(configuration.getString(
				PROPERTY_PREFIX + "alphaBetaTesting", ""));
		this.kidsPackage.setInstallationMessage(configuration.getString(
				PROPERTY_PREFIX + "installationMessage", ""));
		this.kidsPackage.setAddressForUsageReporting(configuration.getString(
				PROPERTY_PREFIX + "addressForUsageReporting", ""));
		this.kidsPackage.setPackageName(configuration.getString(PROPERTY_PREFIX
				+ "packageName", ""));
		this.kidsPackage.setPackageNumber(configuration.getInt(PROPERTY_PREFIX
				+ "packageNumber", 0));
		this.kidsPackage.setPackagePrefix(configuration.getString(
				PROPERTY_PREFIX + "packagePrefix", ""));
		this.kidsPackage.setPackageShortDescription(configuration.getString(
				PROPERTY_PREFIX + "packageShortDescription", ""));
		this.kidsPackage.setPackageVersion(configuration.getString(
				PROPERTY_PREFIX + "packageVersion", ""));
		this.kidsPackage.setPackageDateDistributed(configuration.getString(
				PROPERTY_PREFIX + "packageDateDistributed", ""));
		this.kidsPackage.setPackageDateInstalled(configuration.getString(
				PROPERTY_PREFIX + "packageDateInstalled", ""));
		this.kidsPackage.setPackageInstalledBy(configuration.getString(
				PROPERTY_PREFIX + "packageInstalledBy", ""));
		this.startingKernelIen = configuration.getInt(PROPERTY_PREFIX
				+ "startingKernelIen", 1);
		configurePaths();
		configureBuildLog();
	}

	private void configureBuildLog() {
		try {
			FileAppender appender = new FileAppender(new PatternLayout(
					"%-4r [%t] %-5p %c %x - %m%n"),
					this.outputLogPath.toString());
			Logger.getRootLogger().addAppender(appender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
	}

	private void configurePaths() {
		this.manifestPath = Paths.get(configuration.getString(PROPERTY_PREFIX
				+ "manifest"));

		String outputDir = configuration.getString(PROPERTY_PREFIX
				+ "outputDirectory", null);
		if (outputDir == null || outputDir.equals(""))
			outputDir = this.manifestPath.getParent().toString();
		this.outputDirPath = Paths.get(outputDir);
		this.outputDirPath.toFile().mkdirs();

		String fileName = configuration.getString(PROPERTY_PREFIX
				+ "outputFileName", null);
		if (fileName == null || fileName.equals(""))
			fileName = FilenameUtils.removeExtension(this.manifestPath
					.getFileName().toString());
		if (!fileName.toUpperCase().endsWith(".KID"))
			fileName = fileName + ".KID";
		this.outputFilePath = this.outputDirPath.resolve(fileName);

		this.outputLogPath = this.outputDirPath.resolve(FilenameUtils
				.removeExtension(fileName) + ".log");

		this.componentRootPath = manifestPath
				.getParent()
				.resolve(
						configuration.getString(PROPERTY_PREFIX
								+ "rootDirectory", ".")).normalize();
		searchPaths = new LinkedList<Path>();
		for (String dir : configuration.getStringArray(PROPERTY_PREFIX
				+ "searchDirectories")) {
			searchPaths.add(componentRootPath.resolve(dir));
		}
		if (searchPaths.size() == 0) {
			searchPaths.add(componentRootPath);
		}
	}

	private Path findComponentFile(ManifestComponent component) {
		Path target = null;
		for (Path dir : this.searchPaths) {
			target = dir.resolve(component.getExport());
			if (target.toFile().exists())
				break;
			else
				target = null;
		}
		return target;
	}

	public void loadComponents() {
		for (ManifestComponent component : this.manifest.getComponents()
				.getAllComponents()) {
			Path componentPath = findComponentFile(component);
			if (componentPath == null) {
				logger.error("File not found: " + component.getExport());
			} else {

				ComponentParser parser = ComponentParserFactory
						.CreateParser(componentPath.toFile());
				kidsPackage.addAll(parser.parse(component.getName(),
						componentPath.toFile()));
			}
		}
	}

	public void loadKidsAttributes() {
		this.kidsPackage.name = manifest.getName();
		this.kidsPackage.patch = manifest.getPatch();
		this.kidsPackage.patchName = manifest.getPatchName();
		this.kidsPackage.envCheckRoutine = manifest.getEnvCheckRoutine();
		this.kidsPackage.deleteEnvCheckRoutine = manifest
				.getDeleteEnvCheckRoutine();
		this.kidsPackage.preInstallRoutine = manifest.getPreInstallRoutine();
		this.kidsPackage.deletePreInstallRoutine = manifest
				.getDeletePreInstalltRoutine();
		this.kidsPackage.postInstallRoutine = manifest.getPostInstallRoutine();
		this.kidsPackage.deletePostInstallRoutine = manifest
				.getDeletePostInstallRoutine();
		this.kidsPackage.patch = manifest.getPatch();
		this.kidsPackage.patchName = manifest.getPatchName();
		if (manifest.getPackageName() != null
				&& !manifest.getPackageName().equals(""))
			this.kidsPackage.setPackageName(manifest.getPackageName());
		if (manifest.getPackageNumber() > 0)
			this.kidsPackage.setPackageNumber(manifest.getPackageNumber());
		if (manifest.getAlphaBetaTesting() != null
				&& !manifest.getAlphaBetaTesting().equals(""))
			this.kidsPackage
					.setAlphaBetaTesting(manifest.getAlphaBetaTesting());
		if (manifest.getInstallationMessage() != null
				&& !manifest.getInstallationMessage().equals(""))
			this.kidsPackage.setInstallationMessage(manifest
					.getInstallationMessage());
		if (manifest.getAddressForUsageReporting() != null
				&& !manifest.getAddressForUsageReporting().equals(""))
			this.kidsPackage.setAddressForUsageReporting(manifest
					.getAddressForUsageReporting());
	}

	public void loadKidsRequiredBuilds() {
		// TODO Should we make sure the list is sorted and unique by required
		// build name? Maybe not
		if (manifest.getRequiredBuilds() != null) {
			this.kidsPackage.getRequiredBuilds().addAll(
					manifest.getRequiredBuilds());
		}

	}

	public void loadManifest() {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(KidsManifest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			manifest = (KidsManifest) unmarshaller.unmarshal(manifestPath
					.toFile());
		} catch (JAXBException e) {
			logger.error(e);
			System.exit(0);
		}
	}

	public void renumberKernelComponents() {
		int ien = startingKernelIen;
		for (KernelComponent component : kidsPackage.getAllKernelComponents()) {
			if (component instanceof Routine) {
				continue; // Exclude routines from renumbering
			}
			component.setIen(ien++);
		}
	}

	public void verifySetup() {
		if (kidsPackage.getPackageName() == null
				|| kidsPackage.getPackageName().equals("")) {
			logger.error("Package Name missing");
			System.exit(1);
		}
		if (kidsPackage.getPackageNumber() <= 0) {
			logger.error("Package Number missing");
			System.exit(1);
		}
	}

	public void writeKids() {
		KidsWriter kidsWriter = new KidsWriter(this.kidsPackage);
		kidsWriter.writeKids(outputFilePath);
		logger.info(outputFilePath);
	}
}
