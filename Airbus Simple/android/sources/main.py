import os
import argparse



# --- Output ---
OUTPUT_DIR = 'dist'
OUTPUT_FILENAME = 'output.md'

# --- Configuration ---
# Add directories you want to completely ignore
# I've added OUTPUT_DIR here so the output directory isn't included in subsequent runs.
IGNORE_DIRS = {
    '.git',
    '.vscode',
    '__pycache__',
    'node_modules',
    'venv',
    '.env',
    OUTPUT_DIR, # Important: ignore the output directory
    'build',
    'db_init.py',
    'instance',
}

# Add specific files you want to ignore by name
IGNORE_FILES = {
    '.DS_Store',
    '.gitignore',
}
# --- End Configuration ---

def list_files_and_content(start_path, output_stream):
    """
    Walks through a directory and writes the content of each file to the output stream.
    
    Args:
        start_path (str): The path to the directory to start walking from.
        output_stream: A file-like object to write the output to.
    """
    # Also ignore the script file itself
    script_name = os.path.basename(__file__)
    IGNORE_FILES.add(script_name)

    for dirpath, dirnames, filenames in os.walk(start_path, topdown=True):
        # Modify dirnames in-place to prevent `os.walk` from descending into ignored directories
        dirnames[:] = [d for d in dirnames if d not in IGNORE_DIRS]

        for filename in sorted(filenames):
            if filename in IGNORE_FILES:
                continue

            full_path = os.path.join(dirpath, filename)
            try:
                # Get the path relative to the starting directory
                relative_path = os.path.relpath(full_path, start_path)
                # Standardize path separators to forward slashes for consistency
                relative_path = relative_path.replace(os.sep, '/')
                
                # Write the header with the filename
                output_stream.write(f"`{relative_path}`\n")
                output_stream.write("```\n")

                # Read and write the file content
                with open(full_path, 'r', encoding='utf-8', errors='ignore') as f:
                    content = f.read()
                    output_stream.write(content)
                
                # Ensure the closing ``` is on a new line
                if content and not content.endswith('\n'):
                    output_stream.write('\n')

            except Exception as e:
                output_stream.write(f"[Error reading file {full_path}: {e}]\n")
            finally:
                # Always write the closing fence and a separator line
                output_stream.write("```\n\n")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Scans a directory and saves the content of all its files to dist/output.md."
    )
    parser.add_argument(
        "directory",
        nargs='?',
        default='.',
        help="The directory to scan. Defaults to the current directory."
    )
    args = parser.parse_args()

    start_directory = args.directory
    
    if not os.path.isdir(start_directory):
        print(f"Error: Directory not found at '{start_directory}'")
    else:
        # Create the output directory if it doesn't exist
        os.makedirs(OUTPUT_DIR, exist_ok=True)
        
        output_path = os.path.join(OUTPUT_DIR, OUTPUT_FILENAME)

        try:
            # Open the output file for writing
            with open(output_path, 'w', encoding='utf-8') as f:
                list_files_and_content(start_directory, f)
            
            # Print a success message to the console
            print(f"✅ Successfully saved project structure to '{output_path}'")

        except IOError as e:
            print(f"Error: Could not write to file '{output_path}'. Reason: {e}")
