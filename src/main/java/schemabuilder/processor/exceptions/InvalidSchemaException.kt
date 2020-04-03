package schemabuilder.processor.exceptions

class InvalidSchemaException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor() : super() {}
}