# JChainZ

JChainZ is a simple yet powerful blockchain implementation in Java. It serves as an educational project for understanding the fundamentals of blockchain technology, including wallet creation, transaction processing, and block mining.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Project](#running-the-project)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Wallet Management**: Create and manage wallets to hold and transfer funds.
- **Transaction Processing**: Send and receive transactions with verification and validation.
- **Blockchain Mining**: Mine new blocks and add them to the blockchain.
- **Transaction Output Management**: Track unspent transaction outputs (UTXOs).
- **Blockchain Validation**: Validate the integrity and authenticity of the blockchain.

## Technologies Used

- Java
- Bouncy Castle (for cryptographic functions)

## Getting Started

### Prerequisites

Ensure you have the following installed:

- Java Development Kit (JDK) 8 or later
- An IDE (like IntelliJ IDEA or Eclipse) for easier development


### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/AllenOps08/JChainZ.git
   ```

2. Navigate to the project directory:

   ```bash
   cd JChainZ
   ```

### Running the Project

1. Open the project in your IDE.
2. Run the `Chain` class, which contains the `main` method to start the application.

   ```java
   public static void main(String[] args) {
       // Entry point of the JChainZ application
   }
   ```

## Project Structure

The project is organized into several packages, each serving a specific purpose:

```
JChainZ/
├── .idea/               # IDE configuration files
├── out/                 # Compiled class files
│   └── production/
│       └── JChainZ/
│           ├── Block/  # Block-related classes
│           ├── Chain/  # Blockchain management classes
│           ├── Transactions/  # Transaction-related classes
│           ├── Utils/  # Utility classes
│           └── Wallet/  # Wallet management classes
└── src/                 # Source code
    ├── Block/          # Block implementation
    ├── Chain/          # Blockchain implementation
    ├── Transactions/    # Transaction implementation
    ├── Utils/          # Utility functions
    └── Wallet/         # Wallet implementation
```

## Usage

1. **Creating Wallets**: Use the `Wallet` class to create and manage wallet instances.
2. **Sending Funds**: Use the `sendFunds` method from the `Wallet` class to transfer funds between wallets.
3. **Mining Blocks**: Utilize the `mineBlock` method in the `Block` class to mine new blocks in the blockchain.
4. **Validating Blockchain**: Call the `validateBlockchain` method to ensure the integrity of the blockchain.

## Contributing

Contributions are welcome! If you would like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature/YourFeature
   ```
3. Make your changes and commit them:
   ```bash
   git commit -m "Add your message"
   ```
4. Push to the branch:
   ```bash
   git push origin feature/YourFeature
   ```
5. Create a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

