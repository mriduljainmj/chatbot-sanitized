type InputProps = {
  placeholder: string;
  type?: string;
  value?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
};

export default function Input({
  placeholder,
  type = "text",
  value,
  onChange,
}: InputProps) {
  return (
    <input
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      className="
        w-full px-3 py-2
        border border-gray-300
        rounded-lg
        focus:outline-none
        focus:ring-2 focus:ring-indigo-500
      "
    />
  );
}