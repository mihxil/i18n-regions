async function iTextExample() {
  await cheerpjInit();

  const lib = await cheerpjRunLibrary("/app/itextpdf-5.5.13.3.jar");

  try {
    const Document = await lib.com.itextpdf.text.Document;
    const Paragraph = await lib.com.itextpdf.text.Paragraph;
    const PdfWriter = await lib.com.itextpdf.text.pdf.PdfWriter;
    const FileOutputStream = await lib.java.io.FileOutputStream;

    const document = await new Document();
    const writer = await PdfWriter.getInstance(
      document,
      await new FileOutputStream("/files/HelloIText.pdf")
    );

    await document.open();
    await document.add(await new Paragraph("Hello World!"));
    await document.close();
    await writer.close();

     console.log("PDF created successfully at /files/HelloIText.pdf");
  } catch (e) {
    const IOException = await lib.java.io.IOException;

    if (e instanceof IOException) console.log("I/O error");
    else console.log("Unknown error: " + (await e.getMessage()));
  }
}

await iTextExample()
