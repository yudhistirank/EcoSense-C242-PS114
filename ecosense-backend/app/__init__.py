from flask import Flask, jsonify
from flask_cors import CORS
from .exceptions.client_error import ClientError
from .exceptions.input_error import InputError
from .services.load_model import load_model
from .routes.routes import bp
from asgiref.sync import async_to_sync

def create_app():
    app = Flask(__name__)
    CORS(app)

    # Configure maximum file size (1MB)
    app.config['MAX_CONTENT_LENGTH'] = 1 * 1024 * 1024

    # Register blueprint
    app.register_blueprint(bp)

    # Load model
    @app.before_first_request
    def load_ml_model():
        app.model = async_to_sync(load_model)()

    # Error handlers
    @app.errorhandler(ClientError)
    def handle_client_error(error):
        response = jsonify({
            'status': 'fail',
            'message': error.message
        })
        response.status_code = error.status_code
        return response

    @app.errorhandler(InputError)
    def handle_input_error(error):
        response = jsonify({
            'status': 'fail',
            'message': error.message
        })
        response.status_code = error.status_code
        return response

    @app.errorhandler(Exception)
    def handle_error(error):
        response = jsonify({
            'status': 'fail',
            'message': str(error)
        })
        response.status_code = 500
        return response

    return app