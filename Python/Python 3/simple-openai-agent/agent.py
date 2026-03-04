import os
import json

class Agent:

    def __init__(self):
        self.setup_tools()
        self.messages = [
            {
                "role": "system",
                "content": "You are a helpful English speaker and use very concise responses"
            }
        ]


    def setup_tools(self):
        self.tools = [
            {
                "type": "function",
                "name": "list_files_in_dir",
                "description": "List the files in the input directory (the default is the current directory)",
                "parameters": {
                    "type": "object",
                    "properties": {
                        "directory": {
                            "type": "string",
                            "description": "Directory to walk through (optional). Default value is the current directory."
                        }
                    },
                    "required": []
                }
            },
            {
                "type": "function",
                "name": "read_file",
                "description": "Read the file content from a specified path",
                "parameters": {
                    "type": "object",
                    "properties": {
                        "path": {
                            "type": "string",
                            "description": "Path of the file."
                        }
                    },
                    "required": [
                        "path"
                    ]
                }
            },
            {
                "type": "function",
                "name": "edit_file",
                "description": "Edit the specified file (using its path) by overriding the previous text (prev_text) with a new text (new_text). If the file doesn't exist it creates a new one.",
                "parameters": {
                    "type": "object",
                    "properties": {
                        "path": {
                            "type": "string",
                            "description": "Path of the file."
                        },
                        "prev_text": {
                            "type": "string",
                            "description": "Text to override (it can be empty for new files)."
                        },
                        "new_text": {
                            "type": "string",
                            "description": "Text that overrides prev_text. In the case prev_text is empty, new_text will be the content of a new file."
                        }
                    },
                    "required": [
                        "path",
                        "new_text"
                    ]
                }
            }
        ]


    def list_files_in_dir(self, directory="."):
        print("Tool calling: list_files_in_dir")

        try:
            files = os.listdir(directory)
            return { "files": files }
        except Exception as e:
            return { "error": str(e) }


    def read_file(self, path):
        print("Tool calling: read_file")

        try:
            with open(path, encoding="utf-8") as f:
                return f.read()
        except Exception as e:
            err = f"Error on reading file {path}"
            print(err)
            return err


    def edit_file(self, path, prev_text, new_text):
        print("Tool calling: edit_file")

        try:
            existed = os.path.exists(path)

            if existed and prev_text:
                content = self.read_file(path)

                if prev_text not in content:
                    return f"Text {prev_text} not found in file {path}"

                content = content.replace(prev_text, new_text)
            else:
                dir_name = os.path.dirname(path)

                if dir_name:
                    os.makedirs(dir_name, exist_ok=True)

                content = new_text

            with open(path, "w", encoding="utf-8") as f:
                f.write(content)

            action = "Edited" if existed and prev_text else "Created"
            return f"File {path} [{action}]"
        except Exception as e:
            err = f"Error on editing/reading file {path}"
            print(err)
            return err


    def process_response(self, response):
        self.messages += response.output

        for output in response.output:
            if output.type == "function_call":
                fn_name = output.name
                args = json.loads(output.arguments)

                print(f"    - The model decides to call the tool {fn_name}")
                print(f"    - Arguments {args}")

                if fn_name == "list_files_in_dir":
                    result = self.list_files_in_dir(**args)
                elif fn_name == "read_file":
                    result = self.read_file(**args)
                elif fn_name == "edit_file":
                    result = self.edit_file(**args)

                self.messages.append({
                    "type": "function_call_output",
                    "call_id": output.call_id,
                    "output": json.dumps({
                        "files": result
                    })
                })

                return True
            elif output.type == "message":
                reply = "\n".join(part.text for part in output.content)
                print(f"Assistant: {reply}")

                return False