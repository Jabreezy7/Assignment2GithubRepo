# AutoComplete Tool

## Overview

The AutoComplete Tool is a simple automatic word-completion system designed to assist users in completing words as they type. Built using a DLB (Doubly Linked List) trie structure, this tool allows for efficient word predictions based on user input. The system not only predicts words but also provides functionality for adding new words to its dictionary dynamically.

## Table of Contents

- [Purpose](#purpose)
- [Features](#features)
- [Usage](#usage)

## Purpose

The purpose of this tool is to:
- Implement efficient algorithms for DLB insertion and traversal.
- Retrieve word predictions based on user input in real time.
  
## Features

- **Dynamic Word Prediction**: As the user types, the tool suggests possible word completions based on a predefined dictionary.
- **Word Addition**: Users can add new words to the dictionary if their desired word is not found in the suggestions.
- **Prefix Management**: The tool maintains a current prefix string, allowing for seamless word completion based on partial input.

## Usage
```
java AutoComplete dict8.txt
```


