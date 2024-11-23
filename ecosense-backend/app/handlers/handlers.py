from flask import request, jsonify, current_app
from ..services.inference_service import predict_classification
from ..services.store_data import store_data
from ..services.get_history import get_history
import uuid
from datetime import datetime
from asgiref.sync import async_to_sync

def post_predict_handler():
    image = request.files['image'].read()
    model = current_app.model

    # Wrap async function dengan async_to_sync
    result = async_to_sync(predict_classification)(model, image)
    id = str(uuid.uuid4())
    created_at = datetime.utcnow().isoformat()

    data = {
        "id": id,
        "result": result['label'],
        "suggestion": result['suggestion'],
        "createdAt": created_at
    }

    # Wrap async function dengan async_to_sync
    async_to_sync(store_data)(id, data)

    return jsonify({
        'status': 'success',
        'message': 'Model is predicted successfully',
        'data': data
    }), 201

def get_predict_histories_handler():
    # Wrap async function dengan async_to_sync
    history = async_to_sync(get_history)()
    
    format_history = []
    for doc in history:
        format_history.append({
            'id': doc['id'],
            'history': {
                'result': doc['result'],
                'createdAt': doc['createdAt'],
                'suggestion': doc['suggestion'],
                'id': doc['id']
            }
        })

    return jsonify({
        'status': 'success',
        'data': format_history
    }), 200