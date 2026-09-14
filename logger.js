// Logger class
class Logger {
    log(message) {
        console.log("LOG:", message);
    }
}

// Inject decorator
function Inject(dependency) {
    return function (target) {
        target.prototype.logger = new dependency();
    };
}

// UserService depends on Logger
@Inject(Logger)
class UserService {

    createUser(name) {
        this.logger.log("Creating user: " + name);
        console.log("User created successfully: " + name);
    }
}

// Create UserService object
const userService = new UserService();

// Create a user
userService.createUser("Aruna");
