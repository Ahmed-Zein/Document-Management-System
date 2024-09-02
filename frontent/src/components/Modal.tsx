interface ModalProps {
  isOpen: boolean;
  onClose: () => void | null;
  children: JSX.Element | JSX.Element[] | string;
}


const Modal: React.FC<ModalProps> = ({ isOpen, onClose, children }) => {
    if (!isOpen) return null;
  
    return (
      <div
        className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50"
        onClick={onClose}
      >
        <div
          className="bg-white w-full max-w-md p-6 rounded-lg shadow-lg"
          onClick={(e) => e.stopPropagation()} // Prevent click from closing modal
        >
          {children}
        </div>
      </div>
    );
  };
  
export default Modal;
