type ButtonProps = {
  text: string;
  onClick?: () => void;
};

export default function Button({ text, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="
        w-full py-2 px-4
        bg-indigo-600 text-white
        rounded-lg
        hover:bg-indigo-700
        transition
      "
    >
      {text}
    </button>
  );
}