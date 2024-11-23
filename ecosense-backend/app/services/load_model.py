import tensorflow as tf
import os

async def load_model():
    model_path = os.getenv('MODEL_URL')
    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    return interpreter