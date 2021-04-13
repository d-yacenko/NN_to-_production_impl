import cv2
from PIL import Image
import numpy as np


def byte2image(byte):
    arr = np.frombuffer(byte, dtype=np.uint8)
    image = cv2.imdecode(arr, cv2.IMREAD_COLOR)
    image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
    im_pil = Image.fromarray(image)
    return im_pil

