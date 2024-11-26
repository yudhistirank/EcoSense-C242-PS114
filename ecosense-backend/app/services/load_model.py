import tensorflow as tf
import os
import requests
import tempfile
from urllib.parse import urlparse

async def load_model():
    model_path = os.getenv('MODEL_URL')
    
    try:
        # Check use Local or URL Path
        is_url = bool(urlparse(model_path).scheme)
        
        if is_url:
            # URL (Google Cloud Storage)
            print("Loading model from URL...")
            response = requests.get(model_path)
            response.raise_for_status()
            
            # Save the model temporarily
            with tempfile.NamedTemporaryFile(delete=False) as f:
                f.write(response.content)
                temp_model_path = f.name
            
            # Load model from file temporary
            interpreter = tf.lite.Interpreter(model_path=temp_model_path)
            interpreter.allocate_tensors()
            
            # Delete file temporary
            os.unlink(temp_model_path)
            
        else:
            # Local Path
            print("Loading model from local path...")
            if not os.path.exists(model_path):
                raise Exception(f"Model file not found at {model_path}")
                
            interpreter = tf.lite.Interpreter(model_path=model_path)
            interpreter.allocate_tensors()
            
        print("Model loaded successfully!")
        return interpreter
        
    except Exception as e:
        raise Exception(f"Failed to load model: {str(e)}")