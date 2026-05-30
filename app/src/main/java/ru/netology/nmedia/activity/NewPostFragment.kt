package ru.netology.nmedia.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel


class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    private var fragmentBinding: FragmentNewPostBinding? = null
    private var currentImageUri: Uri? = null // Переменная для хранения ссылки на фото

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        fragmentBinding = binding


//        arguments?.textArg
//            ?.let(binding.edit::setText)


        viewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.buttonGallery.setOnClickListener {
            openGalery()
        }

        binding.buttonCamera.setOnClickListener {
            openCamera()
        }


        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.new_post, menu)
            }

            // так делаем меню для фрагмента
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.save -> {
                            fragmentBinding.let {
                                viewModel.changeContent(it?.edit?.text.toString())
                                viewModel.save()
                                AndroidUtils.hideKeyboard(requireView())
                            }
                            true
                        }

                        else -> false
                    }

            }, viewLifecycleOwner)
            return binding.root
        }

    // 1. Создаем переменную для хранения ссылки на картинку


    // Этот метод вызывается, когда камера вернула результат
    private val pickPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    // 1. Получаем адрес (Uri) изображения
                    val uri = result.data?.data

                    if (uri != null) {
                        // 2. Сохраняем адрес в переменную (на случай, если пригодится позже)
                        viewModel.setSelectedImageUri(uri)

                        // 3. Проверяем, что наш binding (связка с UI) существует
                        // и что адрес картинки не пустой.
                        if (fragmentBinding != null && uri != null) {
                            // 4. Загружаем изображение в ImageView с помощью Glide.
                            //    Glide сам разберётся, как отобразить картинку по адресу.
                            Glide.with(this)
                                .load(uri)
                                .centerCrop()
                                // Масштабирует картинку под размер ImageView
                                .into(fragmentBinding!!.imageView) // Помещаем в нужный ImageView

                            // 5. Делаем ImageView видимым на экране
                            fragmentBinding?.imageView?.visibility = View.VISIBLE
                        }
                    }
                }

                ImagePicker.RESULT_ERROR -> {
                    Snackbar.make(
                        requireView(),
                        "Ошибка при получении изображения",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                else -> {
                    Snackbar.make(requireView(), "Действие отменено", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    fun openGalery() {
        ImagePicker.with(this) // this - ссылка на фрагмент
            .crop() // Включаем кадрирование изображения после съемки
            .compress(2048) // Максимальный размер изображения 2MB
            .provider(ImageProvider.GALLERY) // Указываем, что используем галерею
            .galleryMimeTypes(arrayOf("image/*")) // Типы MIME для галереи (если нужно)
            .createIntent { intent ->
                // Запускаем интент для камеры
                pickPhotoLauncher.launch(intent)
            }
    }

    fun openCamera() {
        ImagePicker.with(this) // this - ссылка на фрагмент
            .crop() // Включаем кадрирование изображения после съемки
            .compress(2048) // Максимальный размер изображения 2MB
            .provider(ImageProvider.CAMERA) // Указываем, что используем камеру
            .galleryMimeTypes(arrayOf("image/*")) // Типы MIME для галереи (если нужно)
            .createIntent { intent ->
                // Запускаем интент для камеры
                pickPhotoLauncher.launch(intent)
            }
    }
}


