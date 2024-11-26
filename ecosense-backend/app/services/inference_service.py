import numpy as np
from PIL import Image
import io
from ..exceptions.input_error import InputError
import cv2

# Define image labels
class_labels = ['Anorganik', 'Organik']

# Define suggestions for each class
suggestions = {
    'Organik': "Sampah ini bisa dikompos dan diolah menjadi pupuk!",
    'Anorganik': "Sampah ini harus didaur ulang dengan benar."
}

async def predict_classification(interpreter, image_bytes):
    try:
        # Convert image bytes to OpenCV format
        nparr = np.frombuffer(image_bytes, np.uint8)
        image = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

        # Preprocess image
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image = cv2.resize(image, (150, 150))
        image = image.astype(np.float32)
        image = image / 255.0
        image = np.expand_dims(image, axis=0)

        # Set input tensor
        input_details = interpreter.get_input_details()
        interpreter.set_tensor(input_details[0]['index'], image)

        # Run inference
        interpreter.invoke()

        # Get prediction
        output_details = interpreter.get_output_details()
        predictions = interpreter.get_tensor(output_details[0]['index'])[0]
        
        # Get predicted class index
        predicted_class_index = int(predictions[0] > 0.5)  # 0 for Organik, 1 for Anorganik
        
        # Get predicted class label
        predicted_label = class_labels[predicted_class_index]
        
        # Get confidence score
        confidence = float(predictions[0]) if predicted_class_index == 1 else float(1 - predictions[0])
        confidence = confidence * 100  # Convert to percentage
        
        # Get suggestion for predicted class
        suggestion = suggestions[predicted_label]

        return {
            'label': predicted_label,
            'confidence': confidence,
            'suggestion': suggestion
        }

    except Exception as error:
        print(f"Error during prediction: {str(error)}")  # Logging untuk debugging
        raise InputError('Terjadi kesalahan dalam melakukan prediksi')