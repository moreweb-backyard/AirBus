#!/usr/bin/env python3
# generate_tree.py

import os
import argparse

# --- Configuration ---
# Directories to ignore during traversal
DEFAULT_IGNORE_DIRS = {".git", "__pycache__", ".vscode", "node_modules", ".idea"}
# Files to ignore
DEFAULT_IGNORE_FILES = {".DS_Store"}
# Default output filename
DEFAULT_OUTPUT_FILE = "output-t.md"

def generate_tree(start_path, ignore_dirs, ignore_files):
    """
    Generates a directory tree structure starting from start_path.

    Args:
        start_path (str): The root directory from which to start the tree.
        ignore_dirs (set): A set of directory names to ignore.
        ignore_files (set): A set of file names to ignore.

    Returns:
        list: A list of strings, where each string is a line in the tree.
    """
    tree_lines = []
    
    # Ensure the start path is a valid directory
    if not os.path.isdir(start_path):
        return [f"Error: Directory not found at '{start_path}'"]

    # Add the root directory to the tree display
    tree_lines.append(f"{os.path.basename(os.path.abspath(start_path))}/")

    # The recursive helper function
    def _tree_generator(directory, prefix=""):
        """A recursive generator to yield the tree structure."""
        # Get all items in the directory and sort them
        try:
            items = sorted(os.listdir(directory))
        except OSError as e:
            # Handle cases where we don't have permission to read a directory
            yield f"{prefix}├── [Error: {e.strerror}]"
            return
            
        # Filter out ignored files and directories
        filtered_items = [
            item for item in items
            if item not in ignore_files and (
                os.path.isfile(os.path.join(directory, item)) or 
                (os.path.isdir(os.path.join(directory, item)) and item not in ignore_dirs)
            )
        ]

        # Use pointers to represent the tree structure
        pointers = ["├── "] * (len(filtered_items) - 1) + ["└── "]
        
        for pointer, item in zip(pointers, filtered_items):
            path = os.path.join(directory, item)
            yield prefix + pointer + item
            
            if os.path.isdir(path):
                # Determine the extension for the prefix for the next level
                extension = "│   " if pointer == "├── " else "    "
                # Recurse into the subdirectory
                yield from _tree_generator(path, prefix + extension)

    # Start the generation process
    tree_generator = _tree_generator(start_path)
    tree_lines.extend(tree_generator)
    
    return tree_lines

def main():
    """Main function to parse arguments and run the script."""
    parser = argparse.ArgumentParser(
        description="Generate a directory tree and save it to a Markdown file.",
        formatter_class=argparse.RawTextHelpFormatter
    )
    
    parser.add_argument(
        "root_dir",
        nargs="?",
        default=".",
        help="The root directory to start generating the tree from. Defaults to the current directory."
    )
    
    parser.add_argument(
        "-o", "--output",
        default=DEFAULT_OUTPUT_FILE,
        help=f"The name of the output Markdown file. Defaults to '{DEFAULT_OUTPUT_FILE}'."
    )
    
    parser.add_argument(
        "-i", "--ignore",
        nargs='*',
        default=list(DEFAULT_IGNORE_DIRS | DEFAULT_IGNORE_FILES),
        help="A space-separated list of directory or file names to ignore."
    )
    
    args = parser.parse_args()
    
    # Separate ignored items into directories and files for clarity, though not strictly necessary for this script's logic
    ignore_set = set(args.ignore)
    
    print(f"Starting directory tree generation for: '{os.path.abspath(args.root_dir)}'")
    print(f"Ignoring: {', '.join(sorted(ignore_set))}")
    print(f"Output will be saved to: '{args.output}'")
    
    # Generate the tree
    tree_lines = generate_tree(args.root_dir, ignore_set, ignore_set)
    
    # Format the final output for the Markdown file
    output_content = (
        f"# Directory Tree for `{os.path.abspath(args.root_dir)}`\n\n"
        "```\n"
        + "\n".join(tree_lines)
        + "\n```\n"
    )
    
    # Write the content to the output file
    try:
        with open(args.output, "w", encoding="utf-8") as f:
            f.write(output_content)
        print(f"\n✅ Successfully generated directory tree in '{args.output}'")
    except IOError as e:
        print(f"\n❌ Error writing to file '{args.output}': {e}")

if __name__ == "__main__":
    main()