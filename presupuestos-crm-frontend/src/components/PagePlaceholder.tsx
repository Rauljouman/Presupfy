type PagePlaceholderProps = {
  title: string
  description: string
}

function PagePlaceholder({
  title,
  description,
}: PagePlaceholderProps) {
  return (
    <section className="w-full min-w-0">
      <h1 className="text-2xl font-semibold tracking-tight
        text-primary md:text-3xl">
        {title}
      </h1>

      <p className="mt-2 text-sm text-muted md:text-base">
        {description}
      </p>
    </section>
  )
}

export default PagePlaceholder
