package schemabuilder.processor.exceptions

class InvalidWiringOptionsException : RuntimeException {
    constructor() : super() {}
    constructor(message: String?) : super(message) {}
}