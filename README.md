# Custom Java CLI Toolset: Unix-Style Command Line Tools

## Overview
This project implements a custom Java application that replicates core Unix-style command-line tools such as `cat`, `wc`, `sort`, and `uniq`, using only Java standard libraries. The system processes text files, handles file validation and errors, and supports command chaining via pipes (`|`).

## Features
- **Command-Line Interface (CLI)** mimicking Unix-style tools.
- **Text file processing** (e.g., reading, counting lines, sorting).
- **Command chaining** using pipes (`|`).
- Robust handling of invalid inputs, empty files, and error scenarios.

## Key Commands Implemented:
- `cat`: Outputs the content of a file.
- `wc`: Counts lines, words, and characters in a file.
- `sort`: Sorts file content in a case-sensitive manner.
- `uniq`: Removes consecutive duplicate lines.

## Example Usage
```bash
java MyCLIApp cat file.txt
java MyCLIApp wc -l file.txt
java MyCLIApp sort file.txt | uniq
