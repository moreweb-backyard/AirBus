import sqlite3
import json
import argparse
import os

def convert_sqlite_to_json(db_path, json_path, indent=4):
    """
    Converts an entire SQLite database to a single JSON file.

    Each table in the database becomes a key in the main JSON object,
    and its value is a list of objects, where each object represents a row.

    Args:
        db_path (str): The file path to the SQLite database.
        json_path (str): The file path for the output JSON file.
        indent (int): The indentation level for the output JSON for pretty-printing.
    """
    # 1. Check if the database file exists
    if not os.path.exists(db_path):
        print(f"Error: Database file not found at '{db_path}'")
        return

    try:
        # 2. Connect to the SQLite database
        conn = sqlite3.connect(db_path)
        # Use a dictionary cursor to get column names with values
        conn.row_factory = sqlite3.Row 
        cursor = conn.cursor()

        # 3. Discover all tables in the database
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';")
        table_names = [table['name'] for table in cursor.fetchall()]

        # 4. Create a dictionary to hold all the data
        database_data = {}

        print(f"Found tables: {', '.join(table_names)}")

        # 5. Iterate over each table and fetch its data
        for table_name in table_names:
            print(f"Processing table: {table_name}...")
            # Note: Using f-string for table names is safe here because the names
            # are sourced from the database schema, not external user input.
            cursor.execute(f"SELECT * FROM {table_name}")
            rows = cursor.fetchall()
            
            # Convert each sqlite3.Row object to a standard dictionary
            table_data = [dict(row) for row in rows]
            
            database_data[table_name] = table_data

        # 6. Write the data to a JSON file
        with open(json_path, 'w', encoding='utf-8') as json_file:
            json.dump(database_data, json_file, indent=indent, ensure_ascii=False)

        print(f"\n✅ Successfully converted '{db_path}' to '{json_path}'")

    except sqlite3.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
    finally:
        # 7. Close the database connection
        if 'conn' in locals() and conn:
            conn.close()
            print("Database connection closed.")

def main():
    """
    Main function to handle command-line arguments for the conversion script.
    """
    parser = argparse.ArgumentParser(
        description="Convert an SQLite database file to a JSON file.",
        formatter_class=argparse.RawTextHelpFormatter
    )
    
    parser.add_argument(
        "sqlite_file", 
        help="Path to the input SQLite database file."
    )
    
    parser.add_argument(
        "json_file", 
        nargs='?', # Make the output file optional
        help="Path to the output JSON file. (Optional)\nIf not provided, it will be named after the database file with a .json extension."
    )

    parser.add_argument(
        "--indent", 
        type=int, 
        default=4, 
        help="Indentation level for the JSON output. Use 0 for a compact file. (Default: 4)"
    )

    args = parser.parse_args()
    
    # If the output file path is not provided, create one automatically
    if not args.json_file:
        # 'database.db' -> 'database.json'
        base_name = os.path.splitext(args.sqlite_file)[0]
        args.json_file = base_name + ".json"

    convert_sqlite_to_json(args.sqlite_file, args.json_file, args.indent)

if __name__ == "__main__":
    main()