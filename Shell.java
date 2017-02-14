import java.io.*;
import java.util.*;

/**
 * A java shell program.
 * 
 * @author drew
 * 
 *         COMMANDS RECOGNIZED ************************************************
 *         Exit/Quit - Closes the program.
 * 
 *         !! - repeats last command.
 * 
 *         !# - repeats # command.
 * 
 *         History - Prints the full history of commands entered.
 * 
 *         CD - Goes to Home Directory
 * 
 *         CD .. -Goes up one directory level.
 * 
 *         CD "______" goes to directory if it exists in current directory.
 * 
 *         CWD - Prints the current working Directory.
 * 
 *         Alias + Keyword + Command - Adds a shortcut for a command.
 * 
 *         Keyword - runs command associated with the keyword if it a stored
 *         Alias.
 */
public class Shell
{
	public static void main(String[] args)
	{
		// Creates the command line string to hold what the user enters.
		String commandLine;

		// creates the reader to get the user entry.
		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));

		// Sets the working directories.
		String mainDir = System.getProperty("user.home");
		String curDir = mainDir;
		String tempDir = curDir;

		// List for history.
		ArrayList<String> hist = new ArrayList<String>();

		// List for Alias
		ArrayList<String> aliasList = new ArrayList<String>();

		// Main loop.
		while (true)
		{
			// read what the user enters.
			System.out.print("cmd>");
			try
			{
				// User input
				commandLine = console.readLine();

				// Checks what the user entered against default commands.
				if (commandLine.equals(""))
				{
					continue;
				}
				else if (commandLine.equalsIgnoreCase("exit"))
				{
					System.out.println("Exiting Shell");
					System.exit(0);
				}
				else if (commandLine.equalsIgnoreCase("quit"))
				{
					System.out.println("Exiting Shell");
					System.exit(0);
				}
				// Command for recalling last command.
				else if (commandLine.equals("!!"))
				{
					commandLine = hist.get(hist.size() - 1);
				}
				// Command for recalling a specific command.
				// ****************************
				else if (commandLine.equals("!"
						+ Integer.getInteger(commandLine.replaceAll("[^0-9]",
								""))))
				{
					commandLine = hist.get(Integer.getInteger(commandLine
							.replaceAll("[^0-9]", "")));
				}
				// Command for displaying history of commands.
				else if (commandLine.equalsIgnoreCase("history"))
				{
					for (int i = 0; i < hist.size(); i++)
					{
						System.out.println(i + "\t" + hist.get(i));
					}
					continue;
				}

				// Create an array list to store the separate commands in.
				ArrayList<String> cmds = new ArrayList<String>();
				String[] inputSplit = commandLine.split(" ");

				// Create an array list to store the history of commands in.
				hist.add(commandLine);

				// Create a counter for the number of inputs.
				int number = inputSplit.length;

				// Add the input parts to the list.
				for (int i = 0; i < number; i++)
				{
					inputSplit[i].trim();
					cmds.add(inputSplit[i]);
				}
				// Loops through all entries in the Alias list and checks to see
				// if any
				// previously saved command is stored which corresponds to
				// entered command.
				for (int y = 0; y < aliasList.size(); y++)
				{
					// Temporary Alias List to check keywork from.
					ArrayList<String> tempAL = new ArrayList<String>();
					String[] fill = aliasList.get(y).split(" ");

					// Loops through array formatting and adding to the
					// temporary alias list.
					for (int z = 0; z < fill.length; z++)
					{
						fill[z].trim();
						tempAL.add(fill[z]);
					}
					// Checks to see if the first entry in the commands list
					// corresponds to the keyword location on the temporary
					// list. If they match, saves the temporary alias list to
					// the command list and removes the keyword.
					if (cmds.get(0).equalsIgnoreCase(tempAL.get(0)))
					{
						cmds = tempAL;
						cmds.remove(0);
					}
				}

				// Check commands entered.
				if (cmds.get(0).equalsIgnoreCase("cd"))
				{
					// Checks to see if only cd was entered.
					if (cmds.size() < 2)
					{
						curDir = mainDir;
					}

					// Checks to see if ".." is after "cd".
					else if (cmds.get(1).equalsIgnoreCase(".."))
					{
						curDir = tempDir;
					}
					else
					{
						// Checks to see if the desired directory exists in the
						// current directory. If it does, adds the desired
						// directory to the current directory.
						if (new File((curDir + "/" + cmds.get(1))).exists())
						{
							tempDir = curDir;
							curDir += ("/" + cmds.get(1));
						}
						else
						{
							System.out
									.println("Could not find directory in current directory.");
						}
					}

				}
				// Checks for wanting to add an Alias shortcut.
				else if (cmds.get(0).equalsIgnoreCase("alias"))
				{
					String temp = "";
					// Adds everything but "Alias" to the Alias List.
					for (int x = 1; x < cmds.size(); x++)
					{
						temp += (cmds.get(x) + " ");
					}

					aliasList.add(temp);
				}
				// Adds a command to find the current working directory.
				else if (cmds.get(0).equalsIgnoreCase("cwd"))
				{
					System.out.println(curDir);
				}
				// Otherwise, sends the commend to the ProcessBuilder.
				else
				{

					// Start the process.
					ProcessBuilder pb = new ProcessBuilder(cmds);
					pb.directory(new File(curDir));
					Process mainP = pb.start();

					// Receive input stream
					InputStream returned = mainP.getInputStream();
					InputStreamReader reader = new InputStreamReader(returned);
					BufferedReader br = new BufferedReader(reader);

					// Print returned values.
					String line;
					while ((line = br.readLine()) != null)
					{
						System.out.println(line);
					}

					br.close();
				}
			}
			catch (IOException e)
			{
				// Prints out an error if the commend is not recognized and
				// continues the program.
				System.out.println("Error, could not recognize command.");
				continue;
			}
		}
	}
}
