const { MongoClient } = require('mongodb');

// Connection URI
const uri = 'mongodb://127.0.0.1:27017';

// Database Name
const dbName = 'mango_db';

// Create a new MongoClient
const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true });

async function initializeDatabase() {
  try {
    // Connect to the MongoDB server
    await client.connect();

    // Create a new database
    const database = client.db(dbName);

    // Create collections
    await database.createCollection("auth", {
        validator: {
          $jsonSchema: {
            bsonType: "object",
            required: ["username", "pass"],
            properties: {
              username: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              pass: {
                bsonType: "string",
                description: "must be a string and is required"
              }
            }
          }
        }
      });

    await database.createCollection("employees", {
        validator: {
          $jsonSchema: {
            bsonType: "object",
            required: ["id", "fname", "lname", "username", "cellnum", "email", "projects", "type"],
            properties: {
              id: {
                bsonType: "int",
                description: "must be an int and is required"
              },
              fname: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              lname: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              username: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              cellnum: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              email: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              projects: {
                bsonType: "string",
                description: "must be a string and is required"
              },
              type: {
                bsonType: "string",
                description: "must be a string and is required"
              },
            }
          }
        }
      });
    await database.createCollection("inventory");
    await database.createCollection('projects');

    console.log('Database initialized successfully!');
  } finally {
    // Close the client
    await client.close();
  }
}

// Call the function to initialize the database
initializeDatabase();
