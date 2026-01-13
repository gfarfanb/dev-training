from openai import OpenAI
from dotenv import load_dotenv

from agent import Agent

load_dotenv()

print("My first AI agent")

client = OpenAI()
agent = Agent()

while True:

    user_input = input("You: ").strip()

    if not user_input:
        continue

    if user_input.strip().lower() in ("exit", "bye", "salir", "sayonara"):
        print("See you!")
        break

    agent.messages.append({
        "role": "user",
        "content": user_input
    })

    while True:
        response = client.responses.create(
            model="gpt-5-nano",
            input=agent.messages,
            tools=agent.tools
        )

        called_tool = agent.process_response(response)

        if not called_tool:
            break
