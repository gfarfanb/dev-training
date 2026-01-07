from openai import OpenAI
from dotenv import load_dotenv

load_dotenv()

print("My first AI agent")

client = OpenAI()

response = client.responses.create(
    model="gpt-5-nano",
    input="Tell me the name of 5 main characters in video games (just naming them, nothing else)"
)

print(response.output_text)
