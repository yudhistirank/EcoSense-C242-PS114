from flask import Blueprint, request, jsonify
from ..handlers.handlers import post_predict_handler, get_predict_histories_handler
from ..exceptions.input_error import InputError
from werkzeug.exceptions import RequestEntityTooLarge

bp = Blueprint('main', __name__)

@bp.route('/predict', methods=['POST'])
def predict():
    if not request.files.get('image'):
        raise InputError('No image provided')
    try:
        return post_predict_handler()
    except RequestEntityTooLarge:
        raise InputError('File too large')

@bp.route('/predict/histories', methods=['GET'])
def histories():
    return get_predict_histories_handler()