
=======================================================
Distributed Agent
=======================================================

This agent is a utility for transmitting large data streams in chunks
from multiple hosts. The agent intends to create a Parent-Child based
architecture where the if the agent is required to be deployed on a server
farm of n nodes. The agent will select the first node as the parent or primary
node and the remaining nodes as children. 